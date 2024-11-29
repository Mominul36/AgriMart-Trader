package com.example.agrimarttrader

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrimarttrader.Class.ControlImage
import com.example.agrimarttrader.Class.MyClass
import com.example.agrimarttrader.Model.Fertilizer
import com.example.agrimarttrader.Model.Pesticide
import com.example.agrimarttrader.databinding.ActivityAddFertilizerBinding
import com.example.agrimarttrader.databinding.ActivityAddPesticideBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPesticideActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPesticideBinding
    private lateinit var controlImage: ControlImage
    lateinit var database: DatabaseReference
    var selectedUnit: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddPesticideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }

        controlImage = ControlImage(this, activityResultRegistry, "imagePickerKey")
        database = FirebaseDatabase.getInstance().getReference("Pesticide")

        binding.progressBar.visibility = View.GONE
        binding.mainlayout.visibility = View.VISIBLE

        binding.fertilizerphoto.setOnClickListener {
            controlImage.setImageView(binding.fertilizerphoto)
            controlImage.selectImage()
        }

        binding.btnpublish.setOnClickListener {
            addDataToDatabase()
        }

        val units = listOf("Packet", "Kg", "Bottle","Ml")
        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.unit.adapter = unitAdapter

        binding.unit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedUnit = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun addDataToDatabase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.mainlayout.visibility = View.GONE


        val id = MyClass().getKey().toString()
        val name = binding.fertilizername.text.toString()
        val rate = binding.rate.text.toString()
        val unit = binding.unit.selectedItem.toString()
        val description = binding.description.text.toString()
        val rating: String = "0.0"

        controlImage.uploadImageToFirebaseStorage { success, message ->
            if (success) {
                val pic = message.toString()

                MyClass().getCurrentTrader { trader ->
                    if (trader != null) {
                        val traderId = trader.id.toString()
                        val division = trader.division
                        val district = trader.district
                        val thana = trader.thana

                        val pesticide = Pesticide(id, traderId, name, rate, unit, description, division, district, thana, pic, rating)
                        database.child(id).setValue(pesticide)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Pesticide Added Successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                binding.progressBar.visibility = View.GONE
                                binding.mainlayout.visibility = View.VISIBLE
                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
