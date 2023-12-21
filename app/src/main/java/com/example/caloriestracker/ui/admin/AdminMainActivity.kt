    package com.example.caloriestracker.ui.admin

    import android.app.AlertDialog
    import android.app.Dialog
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.Observer
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.caloriestracker.R
    import com.example.caloriestracker.data.model.FoodItem
    import com.example.caloriestracker.ui.admin.addFood.AddFoodActivity
    import com.example.caloriestracker.ui.admin.addFood.FoodAdapter
    import com.example.caloriestracker.ui.admin.addFood.FoodRepository
    import com.example.caloriestracker.ui.admin.addFood.FoodViewModel
    import com.example.caloriestracker.ui.admin.addFood.FoodViewModelFactory
    import com.google.firebase.firestore.FirebaseFirestore

    class AdminMainActivity : AppCompatActivity() {

        private val db = FirebaseFirestore.getInstance()
        private val foodViewModel: FoodViewModel by viewModels { FoodViewModelFactory(FoodRepository(db)) }
        private lateinit var adapter: FoodAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_admin_main)

            val addMakananAdminButton = findViewById<Button>(R.id.btnAddMakananAdmin)

            addMakananAdminButton?.setOnClickListener {
                val intent = Intent(this, AddFoodActivity::class.java)
                startActivity(intent)
            }

            val recyclerView: RecyclerView = findViewById(R.id.recyclerAddMakananAdmin)
            adapter = FoodAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            foodViewModel.foodList.observe(this, Observer { foodList ->
                adapter.submitList(foodList)
            })

            val foodCollection = db.collection("makanan")

            foodCollection.get().addOnSuccessListener { result ->
                val foodList = ArrayList<FoodItem>()

                for (document in result) {
                    val nama = document.getString("nama")
                    val kalori = document.getDouble("kalori")

                    if (nama != null && kalori != null) {
                        val foodItem = FoodItem(nama, kalori)
                        foodList.add(foodItem)
                    }
                }


                Log.d("FoodList", "Size: ${foodList.size}")

                adapter.notifyDataSetChanged()

            }.addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
            }
        }

        fun onEditClick(view: View) {
            val documentId = "ID_Dokumen_Yang_Akan_Diupdate"

            db.collection("makanan").document(documentId).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val namaMakanan = documentSnapshot.getString("nama")
                        val kalori = documentSnapshot.getDouble("kalori")

                        // Log the content of the document snapshot
                        Log.d("Firestore", "Document Data: $namaMakanan, $kalori")

                        showEditDialog(namaMakanan, kalori)
                    } else {
                        showToast("Document not found. Document ID: $documentId")
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Error fetching data: ${exception.message}")
                }
        }

        private fun showEditDialog(namaMakanan: String?, kalori: Double?) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.edit_food_admin)

            val editTxtNamaMakanan = dialog.findViewById<EditText>(R.id.editTxtNamaMakanan)
            val editTxtDeskripsiMakanan = dialog.findViewById<EditText>(R.id.editTxtDeskripsiMakanan)
            val btnUpdate = dialog.findViewById<Button>(R.id.btnEdit)

            // Check for null values before using them
            if (namaMakanan != null && kalori != null) {
                editTxtNamaMakanan.setText(namaMakanan)
                editTxtDeskripsiMakanan.setText(kalori.toString())
            }

            btnUpdate.setOnClickListener {
                val updatedNama = editTxtNamaMakanan.text.toString()
                val updatedKalori = editTxtDeskripsiMakanan.text.toString().toDoubleOrNull()

                if (updatedKalori != null) {
                    val documentId = "ID_Dokumen_Yang_Akan_Diupdate"

                    val updatedData = hashMapOf(
                        "nama" to updatedNama,
                        "kalori" to updatedKalori,
                    )

                    db.collection("makanan")
                        .document(documentId)
                        .update(updatedData as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.d("EditItem", "Item edited successfully")
                            showToast("Item edited successfully")
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Log.e("EditItem", "Error editing item: ${it.message}")
                            showToast("Gagal memperbarui data di Firebase: ${it.message}")
                        }
                } else {
                    showToast("Kalori harus berupa angka")
                }
            }

            dialog.show()
        }

        fun onDeleteClick(view: View) {
            // Assuming you have a way to identify the specific item/document you want to delete
            val documentId = "ID_Dokumen_Yang_Akan_Dihapus"

            // Show a confirmation dialog before deleting the item
            showDeleteConfirmationDialog(documentId)
        }

        private fun showDeleteConfirmationDialog(documentId: String) {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete") { _, _ ->
                    deleteItem(documentId)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }


        private fun deleteItem(documentId: String) {
            db.collection("makanan").document(documentId)
                .delete()
                .addOnSuccessListener {
                    showToast("Item deleted successfully")
                }
                .addOnFailureListener { exception ->
                    showToast("Error deleting item: ${exception.message}")
                }
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    }



