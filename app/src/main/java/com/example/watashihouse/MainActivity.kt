package com.example.watashihouse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.watashihouse.Favoris.FavorisFragment
import com.example.watashihouse.Home.HomeFragment
import com.example.watashihouse.Login.LoginActivity
import com.example.watashihouse.Profil.LoginFragment
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Search.SearchFragment
import com.example.watashihouse.ShoppingCart.ShoppingCartFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTestView: TextView
    private lateinit var buttonTestView: TextView
    private lateinit var badgeDrawable: BadgeDrawable
    private var badgeNumber = 0
    //lateinit var recyclerViewMeuble: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //badgeDrawable.number = 0
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
        lifecycleScope?.launch{
            badgeDrawable = bottom_nav_view.getOrCreateBadge(R.id.nav_cart).apply {
                number = badgeNumber
                isVisible = true
                backgroundColor = ContextCompat.getColor(applicationContext,R.color.black)
                badgeTextColor = ContextCompat.getColor(applicationContext,R.color.white)
            }
        }
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

        var localStorage = LocalStorage(applicationContext, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getUserProductFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val items = response.body()?.get("items")?.asJsonArray
                    if(items?.size() != 0)
                        badgeNumber = items?.size()!!

                    lifecycleScope?.launch{
                        badgeDrawable = bottom_nav_view.getOrCreateBadge(R.id.nav_cart).apply {
                            number = badgeNumber
                            isVisible = true
                            backgroundColor = ContextCompat.getColor(applicationContext,R.color.black)
                            badgeTextColor = ContextCompat.getColor(applicationContext,R.color.white)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.e("Erreur r??cuperation articles", it) }
                }
        })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if(resultCode !== RESULT_OK){ //le user a cliqu?? sur le bouton revenir en arriere
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

    fun addValueBadgeCount(number: Int){
        badgeDrawable.number = badgeDrawable.number + number
    }

    fun removeValueBadgeCount(number: Int){
        badgeDrawable.number = badgeDrawable.number - number
    }

    fun resetBadgeCount(){
        badgeDrawable.number = 0
    }

    fun refreshCurrentFragment(fragmentId: Int) {
        var currentFragment = supportFragmentManager.findFragmentById(fragmentId)!!
        supportFragmentManager.beginTransaction().detach(currentFragment).commit()
        supportFragmentManager.beginTransaction().attach(currentFragment).commit()
    }


}
