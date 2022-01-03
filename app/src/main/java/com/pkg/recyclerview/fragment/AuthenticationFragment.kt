package com.pkg.recyclerview.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pkg.recyclerview.AuthenticationActivity
import com.pkg.recyclerview.FormActivity
import com.pkg.recyclerview.MainActivity
import com.pkg.recyclerview.R
import com.pkg.recyclerview.databinding.AuthBinding
import com.pkg.recyclerview.model.Task
import com.pkg.recyclerview.network.Api
import kotlinx.coroutines.MainScope

class AuthenticationFragment() : Fragment() {

    private var _binding: AuthBinding? = null
    private val binding get() = _binding!!

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.co.setOnClickListener {
            findNavController().navigate(R.id.loginFrag)
        }
        binding.sub.setOnClickListener {
            findNavController().navigate(R.id.signupFrag)
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