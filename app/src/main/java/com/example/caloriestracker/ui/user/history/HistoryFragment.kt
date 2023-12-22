package com.example.caloriestracker.ui.user.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriestracker.R
import com.example.caloriestracker.databinding.FragmentHistoryBinding
import com.example.caloriestracker.ui.user.dashboard.addFood.FoodViewModel
import com.google.firebase.auth.FirebaseAuth

class  HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!
    private lateinit var foodViewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val adapter = HistoryAdapter(requireContext())
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewRiwayatMakanan)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //makananViewModel
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        foodViewModel.readAllData.observe(viewLifecycleOwner, Observer {makanan ->
            adapter.setData(makanan)

        })


        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid


        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    // Set parameters if needed
                }
            }
    }
}