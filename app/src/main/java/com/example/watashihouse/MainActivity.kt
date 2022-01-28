package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTestView: TextView
    private lateinit var buttonTestView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CreateElement()
        //SetButtonEvent()
    }

    fun CreateElement(){
        //textViewTestView = findViewById(R.id.textViewTest)
        //buttonTestView = findViewById<Button>(R.id.buttonTest)

        val bottom_nav_view = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val favorisFragment = FavorisFragment()
        val shoppingCartFragment = ShoppingCartFragment()
        val profileFragment = ProfileFragment()
        makeCurrentFragment(homeFragment)

        bottom_nav_view.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> makeCurrentFragment(homeFragment)
                R.id.nav_search -> makeCurrentFragment(searchFragment)
                R.id.nav_favorite -> makeCurrentFragment(favorisFragment)
                R.id.nav_cart -> makeCurrentFragment(shoppingCartFragment)
                R.id.nav_profile -> makeCurrentFragment(profileFragment)
            }
            true
        }

        bottom_nav_view.getOrCreateBadge(R.id.nav_cart).apply {
            number = 2
            isVisible = true
            backgroundColor = ContextCompat.getColor(applicationContext,R.color.black)
            badgeTextColor = ContextCompat.getColor(applicationContext,R.color.white)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    fun SetButtonEvent(){
        buttonTestView.setOnClickListener{
            //monCode
            textViewTestView.text = "monString"
        }
    }
}