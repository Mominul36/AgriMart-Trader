package com.example.agrimarttrader

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrimarttrader.Class.MyClass
import com.example.agrimarttrader.Model.Trader
import com.example.agrimarttrader.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    var selectedDivision : String = ""
    var selectedDistrict : String = ""
    var selectedThana : String = ""




    // List of all divisions
    private val divisions = listOf("Division","Dhaka", "Chittagong", "Rajshahi", "Khulna", "Barisal", "Sylhet", "Rangpur", "Mymensingh")

    // Mapping each division to its districts
    private val districtMap = mapOf(
        "Dhaka" to listOf("District","Dhaka", "Gazipur", "Narayanganj", "Tangail", "Kishoreganj", "Manikganj", "Narsingdi", "Munshiganj", "Faridpur", "Gopalganj", "Madaripur", "Rajbari", "Shariatpur"),
        "Chittagong" to listOf("District","Chittagong", "Cox's Bazar", "Comilla", "Brahmanbaria", "Noakhali", "Feni", "Laxmipur", "Khagrachari", "Rangamati", "Bandarban"),
        "Rajshahi" to listOf("District","Rajshahi", "Pabna", "Natore", "Bogura", "Naogaon", "Joypurhat", "Sirajganj", "Chapainawabganj"),
        "Khulna" to listOf("District","Khulna", "Jessore", "Satkhira", "Narail", "Bagerhat", "Jhenaidah", "Magura", "Kushtia", "Chuadanga", "Meherpur"),
        "Barisal" to listOf("District","Barisal", "Patuakhali", "Bhola", "Pirojpur", "Jhalokathi", "Barguna"),
        "Sylhet" to listOf("District","Sylhet", "Moulvibazar", "Habiganj", "Sunamganj"),
        "Rangpur" to listOf("District","Rangpur", "Dinajpur", "Gaibandha", "Kurigram", "Lalmonirhat", "Nilphamari", "Panchagarh", "Thakurgaon"),
        "Mymensingh" to listOf("District","Mymensingh", "Netrokona", "Sherpur", "Jamalpur")
    )



    private val thanaMap = mapOf(
        // Dhaka Division
        "Dhaka" to listOf("Thana","Gulshan", "Banani", "Dhanmondi", "Mirpur", "Tejgaon", "Uttara", "Mohammadpur", "Badda", "Ramna", "Sabujbagh"),
        "Gazipur" to listOf("Thana","Joydebpur", "Kaliakair", "Tongi", "Kapasia", "Sreepur"),
        "Narayanganj" to listOf("Thana","Sonargaon", "Araihazar", "Rupganj", "Siddhirganj", "Bandar"),
        "Tangail" to listOf("Thana","Tangail Sadar", "Gopalpur", "Nagarpur", "Mirzapur", "Basail", "Dhanbari", "Ghatail", "Kalihati"),
        "Kishoreganj" to listOf("Thana","Kishoreganj Sadar", "Bajitpur", "Bhairab", "Hossainpur", "Itna", "Katiadi", "Karimganj"),
        "Manikganj" to listOf("Thana","Manikganj Sadar", "Singair", "Saturia", "Harirampur", "Ghior", "Shibalaya"),
        "Narsingdi" to listOf("Thana","Narsingdi Sadar", "Belabo", "Raipura", "Shibpur", "Monohardi", "Polash"),
        "Munshiganj" to listOf("Thana","Munshiganj Sadar", "Gazaria", "Sreenagar", "Lohajang", "Tongibari", "Sirajdikhan"),
        "Faridpur" to listOf("Thana","Faridpur Sadar", "Nagarkanda", "Bhanga", "Madhukhali", "Boalmari"),
        "Gopalganj" to listOf("Thana","Gopalganj Sadar", "Kotalipara", "Tungipara", "Muksudpur", "Kashiani"),
        "Madaripur" to listOf("Thana","Madaripur Sadar", "Rajoir", "Shibchar"),
        "Rajbari" to listOf("Thana","Rajbari Sadar", "Goalanda", "Pangsha", "Baliakandi"),
        "Shariatpur" to listOf("Thana","Shariatpur Sadar", "Damudya", "Naria", "Bhedarganj"),

        // Chittagong Division
        "Chittagong" to listOf("Thana","Pahartali", "Panchlaish", "Kotwali", "Chandgaon", "Raozan"),
        "Cox's Bazar" to listOf("Thana","Teknaf", "Ramu", "Maheshkhali", "Chakaria"),
        "Comilla" to listOf("Thana","Daudkandi", "Muradnagar", "Chandina", "Brahmanpara"),
        "Brahmanbaria" to listOf("Thana","Brahmanbaria Sadar", "Ashuganj", "Bancharampur", "Nabinagar"),
        "Noakhali" to listOf("Thana","Noakhali Sadar", "Begumganj", "Hatiya", "Senbagh"),
        "Feni" to listOf("Thana","Feni Sadar", "Chhagalnaiya", "Daganbhuiyan"),
        "Laxmipur" to listOf("Thana","Laxmipur Sadar", "Raipur", "Ramganj", "Ramgati"),
        "Khagrachari" to listOf("Thana","Khagrachari Sadar", "Dighinala", "Mahalchhari"),
        "Rangamati" to listOf("Thana","Rangamati Sadar", "Kaptai", "Baghaichhari"),
        "Bandarban" to listOf("Thana","Bandarban Sadar", "Thanchi", "Ruma", "Rowangchhari"),

        // Rajshahi Division
        "Rajshahi" to listOf("Thana","Boalia", "Motihar", "Rajpara", "Shah Makhdum"),
        "Pabna" to listOf("Thana","Santhia", "Ishwardi", "Sujanagar", "Bera"),
        "Natore" to listOf("Thana","Bagatipara", "Baraigram", "Naldanga", "Lalpur"),
        "Bogura" to listOf("Thana","Bogura Sadar", "Shajahanpur", "Dupchanchia"),
        "Naogaon" to listOf("Thana","Naogaon Sadar", "Raninagar", "Manda"),
        "Joypurhat" to listOf("Thana","Joypurhat Sadar", "Panchbibi", "Khetlal"),
        "Sirajganj" to listOf("Thana","Sirajganj Sadar", "Kazipur", "Tarash", "Shahjadpur"),
        "Chapainawabganj" to listOf("Thana","Chapainawabganj Sadar", "Nachole", "Shibganj"),

        // Khulna Division
        "Khulna" to listOf("Thana","Khulna Sadar", "Sonadanga", "Dumuria"),
        "Jessore" to listOf("Thana","Jessore Sadar", "Bagherpara", "Abhaynagar"),
        "Satkhira" to listOf("Thana","Satkhira Sadar", "Tala", "Debhata"),
        "Narail" to listOf("Thana","Narail Sadar", "Kalia", "Lohagara"),
        "Bagerhat" to listOf("Thana","Bagerhat Sadar", "Rampal", "Mongla"),
        "Jhenaidah" to listOf("Thana","Jhenaidah Sadar", "Kotchandpur", "Maheshpur"),
        "Magura" to listOf("Thana","Magura Sadar", "Mohammadpur", "Shalikha"),
        "Kushtia" to listOf("Thana","Kushtia Sadar", "Bheramara", "Mirpur"),
        "Chuadanga" to listOf("Thana","Chuadanga Sadar", "Alamdanga", "Damurhuda"),
        "Meherpur" to listOf("Thana","Meherpur Sadar", "Gangni"),

        // Barisal Division
        "Barisal" to listOf("Thana","Barisal Sadar", "Bakerganj", "Babuganj"),
        "Patuakhali" to listOf("Thana","Patuakhali Sadar", "Bauphal", "Kalapara"),
        "Bhola" to listOf("Thana","Bhola Sadar", "Char Fasson", "Lalmohan"),
        "Pirojpur" to listOf("Thana","Pirojpur Sadar", "Bhandaria", "Mathbaria"),
        "Jhalokathi" to listOf("Thana","Jhalokathi Sadar", "Kathalia", "Rajapur"),
        "Barguna" to listOf("Thana","Barguna Sadar", "Patharghata", "Amtali"),

        // Sylhet Division
        "Sylhet" to listOf("Thana","Sylhet Sadar", "Beanibazar", "Zakiganj"),
        "Moulvibazar" to listOf("Thana","Moulvibazar Sadar", "Srimangal", "Kamalganj"),
        "Habiganj" to listOf("Thana","Habiganj Sadar", "Chunarughat", "Bahubal"),
        "Sunamganj" to listOf("Thana","Sunamganj Sadar", "Tahirpur", "Bishwamvarpur"),

        // Rangpur Division
        "Rangpur" to listOf("Thana","Rangpur Sadar", "Pirganj", "Badarganj"),
        "Dinajpur" to listOf("Thana","Dinajpur Sadar", "Parbatipur", "Birganj"),
        "Gaibandha" to listOf("Thana","Gaibandha Sadar", "Palashbari", "Gobindaganj"),
        "Kurigram" to listOf("Thana","Kurigram Sadar", "Nageshwari", "Bhurungamari"),
        "Lalmonirhat" to listOf("Thana","Lalmonirhat Sadar", "Hatibandha", "Kaliganj"),
        "Nilphamari" to listOf("Thana","Nilphamari Sadar", "Saidpur", "Jaldhaka"),
        "Panchagarh" to listOf("Thana","Panchagarh Sadar", "Tetulia", "Boda"),
        "Thakurgaon" to listOf("Thana","Thakurgaon Sadar", "Pirganj", "Baliadangi"),

        // Mymensingh Division
        "Mymensingh" to listOf("Thana","Mymensingh Sadar", "Trishal", "Muktagachha"),
        "Netrokona" to listOf("Thana","Netrokona Sadar", "Purbadhala", "Kendua"),
        "Sherpur" to listOf("Thana","Sherpur Sadar", "Nakla", "Nalitabari"),
        "Jamalpur" to listOf("Thana","Jamalpur Sadar", "Sarishabari", "Islampur")
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Trader")
        auth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            registerUser()
        }



        val divisionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, divisions)
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.division.adapter = divisionAdapter

        binding.division.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDivision2 = divisions[position]
                loadDistricts(selectedDivision2)

                selectedDivision = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        binding.district.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDistrict2 = binding.district.selectedItem as? String
                selectedDistrict2?.let { loadThanas(it) }
                selectedDistrict = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        // Set up the onItemSelectedListener
        binding.thana.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedThana = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }



















    }

    private fun registerUser() {
        val name = binding.name.text.toString().trim()
        var phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.cpassword.text.toString().trim()

        var division = binding.division.selectedItem.toString()
        var district = binding.district.selectedItem.toString()
        var thana = binding.thana.selectedItem.toString()

        if (name.isEmpty()) {
            binding.name.error = "Name is required"
            return
        }
        if (phone.isEmpty()) {
            binding.phone.error = "Phone number is required"
            return
        }
        if (!phone.matches(Regex("^\\+880\\d{10}$"))) {
            binding.phone.error = "Enter a valid phone number (+880XXXXXXXXXX)"
            return
        }
        if (password.isEmpty() || password.length < 6) {
            binding.password.error = "Password must be at least 6 characters"
            return
        }
        if (password != confirmPassword) {
            binding.cpassword.error = "Passwords do not match"
            return
        }

        phone = phone.replace("+88","")

        auth.createUserWithEmailAndPassword(phone + "@gmail.com", password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val traderId = phone
                    val trader = Trader(traderId, name, phone, "", password, "0",division,district,thana)

                    if (traderId != null) {
                        database.child(traderId).setValue(trader)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                    auth.signOut()
                                    startActivity(Intent(this,LoginActivity::class.java))
                                } else {
                                    Toast.makeText(this, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun loadDistricts(division: String) {
        val districts = districtMap[division] ?: listOf()
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.district.adapter = districtAdapter

    }

    private fun loadThanas(district: String) {
        val thanas = thanaMap[district] ?: listOf()
        val thanaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, thanas)
        thanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.thana.adapter = thanaAdapter
    }



}
