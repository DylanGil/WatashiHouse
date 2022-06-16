package com.example.watashihouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTestView: TextView
    private lateinit var buttonTestView: TextView
    private lateinit var dataStore: DataStore<Preferences>
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

        dataStore = createDataStore(name = "meubleStored")
        var preferencesNumber = 0
        lifecycleScope?.launch{

        val preferences = dataStore.data.first()
            preferencesNumber = preferences.asMap().size

            bottom_nav_view.getOrCreateBadge(R.id.nav_cart).apply {
                number = preferencesNumber
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

    fun SetButtonEvent(){
        buttonTestView.setOnClickListener{
            //monCode
            textViewTestView.text = "monString"
        }
    }
}
