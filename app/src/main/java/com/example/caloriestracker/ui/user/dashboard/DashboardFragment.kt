package com.example.caloriestracker.ui.user.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.caloriestracker.MainActivity
import com.example.caloriestracker.R
import com.example.caloriestracker.data.database.AppDatabase
import com.example.caloriestracker.data.database.UserDao
import com.example.caloriestracker.databinding.FragmentDashboardBinding
import com.example.caloriestracker.ui.user.dashboard.addFood.AddFoodActivityUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var userDao: UserDao
    private lateinit var userId: String

    private val CHANNEL_ID = "CaloriesTrackerNotificationChannel"
    private val CHANNEL_NAME = "Calories Tracker Notification Channel"
    private val PREFS_NAME = "CaloriesTrackerPrefs"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "app-database"
        ).build()

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        userDao = db.userDAO()

        val textView: TextView = binding.TargetKaloriText
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            // Handle perubahan ViewModel jika diperlukan
        }

        userId = firebaseAuth.currentUser?.uid!!
        val userDocRef = userId?.let { firestore.collection("users").document(it) }

        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("username")
                        binding.userWelcomeText.text = "Hai, $username"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle kegagalan untuk mengambil data
                }
        }

        userDocRef?.get()
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    val bb = document.getDouble("bb") ?: 0.0
                    val bbTarget = document.getDouble("bbTarget") ?: 0.0
                    val tb = document.getDouble("tb") ?: 0.0

                    val dailyCalories = calculateDailyCalories(bb, tb, bbTarget)

                    binding.userTargetKaloriValue.text = "$dailyCalories cal"

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val totalCalories = userDao.getTotalCalories(userId)
                            withContext(Dispatchers.Main) {
                                binding.userKonsumsiKaloriValue.text = totalCalories.toString()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                // Tangani exception (misalnya, log atau tampilkan pesan kesalahan)
                            }
                        }
                    }

                    val userRef = firestore.collection("users").document(userId)

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val totalCalories = userDao.getTotalCalories(userId)
                            val document = userRef.get().await()

                            withContext(Dispatchers.Main) {
                                if (document.exists()) {
                                    val target = document.getString("kaloritarget") ?: "0"
                                    val totalCalori = totalCalories

                                    val sisaKalori = target.toInt() - totalCalori
                                    binding.userSisaKaloriValue.text = "$sisaKalori"

                                    var persentarget =
                                        (totalCalori.toDouble() / target.toInt()) * 100
                                    if (persentarget > 100) {
                                        persentarget = 100.0
                                    }

                                    binding.userProgressBar.progress = persentarget.toInt()

                                    clearNotificationStatus()

                                    if (totalCalori > target.toInt() && !isNotificationShown()) {
                                        showNotification(
                                            "Calorie Alert",
                                            "Anda telah melebihi target kalori!"
                                        )
                                        Toast.makeText(
                                            requireContext(),
                                            "Peringatan Kalori: Anda telah melebihi target kalori!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        markNotificationAsShown()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                // Tangani exception (misalnya, tampilkan pesan kesalahan)
                            }
                        }
                    }
                }
            }
            ?.addOnFailureListener { exception ->
                // Tangani kegagalan untuk mengambil data
            }

        initializeAddFoodButton()

        return root
    }

    private fun markNotificationAsShown() {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNotificationShown", true)
        editor.apply()
    }

    private fun showNotification(title: String, content: String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager =
            NotificationManagerCompat.from(requireContext())

        createNotificationChannel(notificationManager)

        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManagerCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi Kalori"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isNotificationShown(): Boolean {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isNotificationShown", false)
    }

    private fun clearNotificationStatus() {
        val sharedPreferences =
            requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("isNotificationShown")
        editor.apply()
    }

    private fun calculateDailyCalories(bb: Double, tb: Double, bbTarget: Double): Int {
        val bmr = 88.362 + (13.397 * bb) + (4.799 * tb) - (5.677 * 25)
        val tdee = bmr

        return when {
            bb < bbTarget -> (tdee + 500).toInt()
            bb >= bbTarget -> (tdee - 500).toInt()
            else -> tdee.toInt()
        }
    }
    private fun initializeAddFoodButton() {
        val addButton: ImageButton = binding.btnAddFood

        addButton.setOnClickListener {
            // Handle the click event when the "Add Food" button is clicked
            startActivity(Intent(requireContext(), AddFoodActivityUser::class.java))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
