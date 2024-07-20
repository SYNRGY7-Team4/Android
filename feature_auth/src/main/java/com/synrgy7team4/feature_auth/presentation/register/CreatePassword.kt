package com.synrgy7team4.feature_auth.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentCreatePasswordBinding
import com.synrgy7team4.feature_auth.databinding.FragmentOtpVerificationBinding

class CreatePassword : Fragment() {

    private var _binding: FragmentCreatePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       //return inflater.inflate(R.layout.fragment_create_password, container, false)
        _binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitCreatedPassword.setOnClickListener {
            view.findNavController().navigate(R.id.action_createPassword_to_ktpVerificationBoardFragment2)
        }

        view.findViewById<MaterialButton>(R.id.submitCreatedPassword).setOnClickListener { getPassword(view) }
    }

    private fun getPassword(view:View){
        val password = view.findViewById<TextInputEditText>(R.id.inputPassword).text
        val passwordConfirmation = view.findViewById<TextInputEditText>(R.id.inputPasswordConfirmation).text

        Log.d("password", password.toString())
        Log.d("passwordConfirmation", passwordConfirmation.toString())
    }
}