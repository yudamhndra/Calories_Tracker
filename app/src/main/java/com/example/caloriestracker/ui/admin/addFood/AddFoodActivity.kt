package com.example.caloriestracker.ui.admin.addFood

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caloriestracker.R
import com.example.caloriestracker.ui.admin.AdminMainActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddFoodActivity : AppCompatActivity() {

    private lateinit var etNamaMakanan: EditText
    private lateinit var etJumlahKalori: EditText
    private lateinit var btnSave: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        etNamaMakanan = findViewById(R.id.etNamaMakanan)
        etJumlahKalori = findViewById(R.id.etJumlahKalori)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            tambahMakanan()
        }
    }

    private fun tambahMakanan() {
        val namaMakanan = etNamaMakanan.text.toString()
        val jumlahKalori = etJumlahKalori.text.toString().toDouble()

        val makanan = hashMapOf(
            "nama" to namaMakanan,
            "kalori" to jumlahKalori
        )

        db.collection("makanan")
            .add(makanan)
            .addOnSuccessListener { documentReference ->
                val makananWithId = hashMapOf(
                    "id" to documentReference.id,
                    "nama" to namaMakanan,
                    "kalori" to jumlahKalori
                )

                // Update dokumen dengan menambahkan ID
                db.collection("makanan").document(documentReference.id)
                    .set(makananWithId)
                    .addOnSuccessListener {
                        etNamaMakanan.text.clear()
                        etJumlahKalori.text.clear()
                        showToast("Makanan berhasil ditambahkan dengan ID: ${documentReference.id}")
                        val intent = Intent(this, AdminMainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        showToast("Makanan gagal ditambahkan: $e")
                    }
            }
            .addOnFailureListener { e ->
                showToast("Makanan gagal ditambahkan: $e")
            }


    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
