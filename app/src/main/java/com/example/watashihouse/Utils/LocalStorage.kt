package com.example.watashihouse.Utils

import android.content.Context
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LocalStorage(context: Context?, dataStoreName: String) {
    val name = dataStoreName
    var jwtToken = ""
    var userId = ""
    var userFirstName = ""
    var userLastName = ""
    var userEmail = ""
    var panierId = ""
    var favorisId = ""
    private val dataStore = context?.createDataStore(name = name)

    init {
        runBlocking {
            launch {
                var jwt = readfromLocalStorage()?.let { JWT (it) }
                jwtToken = jwt.toString()
                userId = jwt?.getClaim("id")?.asString().toString()
                userFirstName = jwt?.getClaim("firstname")?.asString().toString()
                userLastName = jwt?.getClaim("lastname")?.asString().toString()
                userEmail = jwt?.getClaim("email")?.asString().toString()
                panierId = jwt?.getClaim("id_panier")?.asString().toString()
                favorisId = jwt?.getClaim("id_favoris")?.asString().toString()
            }
        }
    }

    suspend fun saveToLocalStorage(key: String, value: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore?.edit { jwt ->
            jwt[dataStoreKey] = value
        }
    }

    suspend fun readfromLocalStorage(): String? {
        val dataStoreKey = preferencesKey<String>(name)
        val preferences = dataStore?.data?.first()
        return preferences?.get(dataStoreKey)
    }

    suspend fun clearLocalStorage(){
        dataStore?.edit {
            it.clear()
        }
    }

    /*fun readJWT() { //just for info
        lifecycleScope.launch {
            var jwt = readfromLocalStorage("jwt")?.let { JWT(it) }
            var jwtFirstName = jwt?.getClaim("firstname")
            var jwtAllClaims = jwt?.claims
            jwtAllClaims?.forEach{element ->
                element.value.asString()?.let { Log.e(element.key, it) }
            }
            jwtFirstName?.asString()?.let { Log.e("jwtFirstName", it) }
            //Toast.makeText(applicationContext, jwtFirstName?.asString(), Toast.LENGTH_SHORT).show()
        }
    }*/
}