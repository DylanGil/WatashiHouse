package com.example.watashihouse

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTestView: TextView
    private lateinit var buttonTestView: TextView
    //lateinit var recyclerViewMeuble: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //recyclerViewMeuble = findViewById(R.id.recyclerViewHome)
        CreateElement()
        //SetButtonEvent()
    }

    fun CreateElement(){
        //textViewTestView = findViewById(R.id.textViewTest)
        //buttonTestView = findViewById<Button>(R.id.buttonTest)

        /**val layout = FrameLayout(this)
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        layout.id = R.id.layout
        setContentView(layout)

        supportFragmentManager.beginTransaction().add(R.id.layout, HomeFragment()).commit()*/

        val bottom_nav_view = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val favorisFragment = FavorisFragment()
        val shoppingCartFragment = ShoppingCartFragment()
        val userFragment = LoginFragment()
        makeCurrentFragment(homeFragment)
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 1)

        bottom_nav_view.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> makeCurrentFragment(homeFragment)
                R.id.nav_search -> makeCurrentFragment(searchFragment)
                R.id.nav_favorite -> makeCurrentFragment(favorisFragment)
                R.id.nav_cart -> makeCurrentFragment(shoppingCartFragment)
                R.id.nav_user -> makeCurrentFragment(userFragment)
            }
            true
        }

        bottom_nav_view.setOnItemReselectedListener {
            when(it.itemId){
                R.id.nav_home -> refreshCurrentFragment(homeFragment.id)
                R.id.nav_search -> refreshCurrentFragment(searchFragment.id)
                R.id.nav_favorite -> refreshCurrentFragment(favorisFragment.id)
                R.id.nav_cart -> refreshCurrentFragment(shoppingCartFragment.id)
                R.id.nav_user -> refreshCurrentFragment(userFragment.id)
            }
        }


        lifecycleScope?.launch{

            bottom_nav_view.getOrCreateBadge(R.id.nav_cart).apply {
                number = 8
                isVisible = true
                backgroundColor = ContextCompat.getColor(applicationContext,R.color.black)
                badgeTextColor = ContextCompat.getColor(applicationContext,R.color.white)
            }
        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if(resultCode !== RESULT_OK){ //le user a cliqué sur le bouton revenir en arriere
                Toast.makeText(applicationContext, "NUUUUUUUL", Toast.LENGTH_SHORT).show()
                this@MainActivity.finish()
                exitProcess(0)
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    fun refreshCurrentFragment(fragmentId: Int) {
        var currentFragment = supportFragmentManager.findFragmentById(fragmentId)!!
        supportFragmentManager.beginTransaction().detach(currentFragment).commit()
        supportFragmentManager.beginTransaction().attach(currentFragment).commit()
    }


}
