package com.pkg.recyclerview.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pkg.recyclerview.MainActivity
import com.pkg.recyclerview.databinding.SignupBinding
import com.pkg.recyclerview.model.LoginForm
import com.pkg.recyclerview.model.RegisterForm
import com.pkg.recyclerview.network.Api
import com.pkg.recyclerview.viewModel.UserInfoViewModel
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private var _binding: SignupBinding? = null
    private val binding get() = _binding!!

    private var viewModel = UserInfoViewModel();
    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.insIns.setOnClickListener {
            val fn = binding.insPrename.text.toString()
            val n = binding.insName.text.toString()
            val mail = binding.insMail.text.toString()
            val mdp = binding.insMdp.text.toString()
            val mdp2 = binding.insMdp2.text.toString()
            if (fn.isEmpty() || n.isEmpty() || mail.isEmpty()|| mdp.isEmpty() || mdp2.isEmpty()) {
                Toast.makeText(context, "Veuillez bien remplir les champs", Toast.LENGTH_LONG).show()
            } else {
                val data = RegisterForm(fn, n, mail, mdp, mdp2)
                lifecycleScope.launch() {
                    val token = viewModel.addAccount(data)
                    if (token == null) {
                        Toast.makeText(context, "Erreur", Toast.LENGTH_LONG).show()
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