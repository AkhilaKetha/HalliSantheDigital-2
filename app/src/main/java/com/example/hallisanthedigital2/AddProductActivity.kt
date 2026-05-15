package com.example.hallisanthedigital2

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddProductActivity : AppCompatActivity() {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val spinner = findViewById<Spinner>(R.id.spinnerCategory)
        val ivPreview = findViewById<ImageView>(R.id.ivPreview)
        val etName = findViewById<EditText>(R.id.etName)
        val etPrice = findViewById<EditText>(R.id.etPrice)

        val categories = listOf("Vegetables", "Grocery", "Fruits", "Handicrafts")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        // Fixed Database Initialization
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "market_db")
            .fallbackToDestructiveMigration()
            .build()
        val pickImg = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                ivPreview.setImageURI(uri)
            }
        }

        findViewById<Button>(R.id.btnPick).setOnClickListener { pickImg.launch("image/*") }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = etName.text.toString()
            val price = etPrice.text.toString()
            val selectedCategory = spinner.selectedItem.toString()

            if (imageUri != null && name.isNotBlank() && price.isNotBlank()) {
                lifecycleScope.launch {
                    val path = saveImageLocally(imageUri!!)
                    val product = Product(name = name, price = price, imagePath = path, category = selectedCategory)
                    db.productDao().insert(product)
                    Toast.makeText(this@AddProductActivity, "Product Added!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageLocally(uri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val file = File(filesDir, "${UUID.randomUUID()}.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out)
        out.close()
        return file.absolutePath
    }
}