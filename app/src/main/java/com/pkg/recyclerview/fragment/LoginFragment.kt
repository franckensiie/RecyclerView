package com.pkg.recyclerview.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pkg.recyclerview.MainActivity
import com.pkg.recyclerview.R
import com.pkg.recyclerview.UserInfoActivity
import com.pkg.recyclerview.databinding.LoginBinding
import com.pkg.recyclerview.model.LoginForm
import com.pkg.recyclerview.model.Task
import com.pkg.recyclerview.network.Api
import com.pkg.recyclerview.viewModel.UserInfoViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE







const val SHARED_PREF_TOKEN_KEY = ""

class LoginFragment : Fragment() {

    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private var viewModel = UserInfoViewModel();
    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.coCo.setOnClickListener {
            val mail = binding.coMail.text.toString()
            val mdp = binding.coMdp.text.toString()
            if (mail.isEmpty() || mdp.isEmpty()) {
                Toast.makeText(context, "Veuillez bien remplir les champs", Toast.LENGTH_LONG).show()
            } else {
                val data = LoginForm(mail, mdp)
                lifecycleScope.launch() {
                    val token = viewModel.getAccount(data)
                    if (token == null) {
                        Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show()
                    } else {
                        val editor = activity!!.getSharedPreferences("TOKEN_SHARE", Context.MODE_PRIVATE).edit()
                        editor.putString("TOKEN", token.token)
                        editor.apply()
                        val intent = Intent(activity, MainActivity::class.java)
                        formLauncher.launch(intent)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Api.TOKEN != "null") {
            val intent = Intent(activity, MainActivity::class.java)
            formLauncher.launch(intent)
        }
    }

}