package com.example.watashihouse

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.watashihouse.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var button: Button
    lateinit var emailTxt: EditText
    lateinit var passwordTxt:EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        button = view.findViewById(R.id.LoginButton) as Button
        emailTxt = view.findViewById(R.id.editTextTextUsername) as EditText
        passwordTxt = view.findViewById(R.id.editTextTextPassword) as EditText

        initAction()

        primaryFunction()
        return view
    }

    private fun initAction() {
        button?.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val request = UserRequest()
        request.email = emailTxt.text.toString().trim()
        request.hash = passwordTxt.text.toString().trim()

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.login(request).enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val user = response.body()
                if(user != null){
                    Toast.makeText(context, "User trouvé", Toast.LENGTH_SHORT).show()
                    Log.e("hash", user!!.token.toString())
                    passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
                }else{
                    Toast.makeText(context, "User inexistant", Toast.LENGTH_SHORT).show()
                }
                /*Log.e("hash", user!!.data?.hash.toString())
                Log.e("email", user!!.data?.email.toString())*/
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(context, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                Log.e("Error", t.message.toString())
            }

        })
    }


    private fun primaryFunction(){
        //binding.favorisText.text = "monFavoris"


    }


}