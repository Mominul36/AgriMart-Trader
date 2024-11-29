package com.example.agrimarttrader


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.agrimarttrader.Fragments.AgrochemicalFragment
import com.example.agrimarttrader.Fragments.MachineryFragment
import com.example.agrimarttrader.Fragments.ProfileFragment
import com.example.agrimarttrader.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var flag: Boolean = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        setFragment(AgrochemicalFragment())

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.rightNavIcon.setOnClickListener {
            if (binding.drawerlayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerlayout.closeDrawer(GravityCompat.END)
            } else {
                binding.drawerlayout.openDrawer(GravityCompat.END)
            }
        }
        binding.navView.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> setFragment(AgrochemicalFragment())
                R.id.nav_search -> setFragment(MachineryFragment())
                R.id.nav_profile -> setFragment(ProfileFragment())
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerlayout.closeDrawer(GravityCompat.END)
        return true
    }
}
