package com.example.watashihouse.Profil

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.watashihouse.Commande.CommandeActivity
import com.example.watashihouse.Login.InscriptionActivity
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.R
import com.example.watashihouse.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var emailTxt: TextView
    lateinit var firstNameTxt: TextView
    lateinit var lastNameTxt: TextView
    lateinit var disconnectButton: Button
    lateinit var editInfoButton: Button
    lateinit var commandeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailTxt = view.findViewById(R.id.tvEmail) as TextView
        firstNameTxt = view.findViewById(R.id.tvFirstName) as TextView
        lastNameTxt = view.findViewById(R.id.tvLastName) as TextView
        disconnectButton = view.findViewById(R.id.disconnectButton) as Button
        editInfoButton = view.findViewById(R.id.editInfoButton) as Button
        commandeButton = view.findViewById(R.id.commandeButton) as Button
        initAction()
        return view
    }

    private fun initAction() {
        lifecycleScope.launch {
            val localStorage = LocalStorage(context, "jwt")
            firstNameTxt.text = localStorage.userFirstName
            lastNameTxt.text = localStorage.userLastName
            emailTxt.text = localStorage.userEmail
        }

        disconnectButton.setOnClickListener {
            runBlocking {
                launch {
                    LocalStorage(context,"jwt").clearLocalStorage()
                    restartApp()
                }
            }
        }

        editInfoButton.setOnClickListener {
            val intent = Intent(activity, InscriptionActivity::class.java)
            startActivity(intent)
        }

        commandeButton.setOnClickListener {
            val intent = Intent(activity, CommandeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun restartApp() {
        val am = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am[AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 500] =
            PendingIntent.getActivity(
                activity, 0, requireActivity().intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        val i = requireActivity().baseContext.packageManager
            .getLaunchIntentForPackage(requireActivity().baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

}