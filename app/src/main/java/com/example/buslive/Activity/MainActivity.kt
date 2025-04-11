package com.example.buslive.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.buslive.Fragment.FragmentAccount
import com.example.buslive.Fragment.FragmentFilter
import com.example.buslive.Fragment.FragmentHistory
import com.example.buslive.Fragment.FragmentHome
import com.example.buslive.Model.addCabinsForTrip
import com.example.buslive.Model.pushMultipleChuyenXeData
import com.example.buslive.Model.pushMultipleTuyenDuongData
import com.example.buslive.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Load fragment mặc định (home)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentHome())
                .commit()
        }

        pushMultipleTuyenDuongData()
        pushMultipleChuyenXeData()

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(FragmentHome())
                    true
                }
                R.id.navigation_filter -> {
                    loadFragment(FragmentFilter())
                    true
                }
                R.id.navigation_history -> {
                    loadFragment(FragmentHistory())
                    true
                }
                R.id.navigation_account -> {
                    loadFragment(FragmentAccount())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
