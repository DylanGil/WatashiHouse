package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTestView: TextView
    private lateinit var buttonTestView: TextView
    private lateinit var bottom_navigation_view: View





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CreateElement()
        //SetButtonEvent()
        teeest()

        //BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
    }

    fun teeest(){
        bottom_navigation_view = findViewById(R.id.bottom_navigation)
        bottom_navigation_view.setOnClickListener{
            when(R.id.bottom_navigation){
                R.id.nav_home -> HomeFragment()
                R.id.nav_favorite -> FavorisFragment()
            }
        }
    }


    fun CreateElement(){
        //textViewTestView = findViewById(R.id.textViewTest)
        //buttonTestView = findViewById<Button>(R.id.buttonTest)
    }

    fun SetButtonEvent(){
        buttonTestView.setOnClickListener{
            //monCode
            textViewTestView.text = "monString"
        }
    }
}