package com.synrgy7team4.feature_auth.presentation.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jer.shared.ViewModelFactoryProvider

import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentInputEmailBinding
import com.synrgy7team4.feature_auth.presentation.viewmodel.RegisterViewModel

class InputEmailFragment : Fragment() {
    private val binding by lazy { FragmentInputEmailBinding.inflate(layoutInflater) }
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<RegisterViewModel> {
//        val app = requireActivity().application
//        (app as MyApplication).viewModelFactory
//
        val app = requireActivity().application as ViewModelFactoryProvider
        app.provideViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAccessibility()

        sharedPreferences =
            requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)

        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            val email = binding.tiedtEmail.text.toString()
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


            when {
                email.isEmpty() -> binding.tiedtEmail.error = "Email Tidak Boleh Kosong!"
                else -> {
                    if (!email.matches(emailPattern.toRegex())) {
                        binding.tiedtEmail.error =
                            "Format email tidak sesuai. Contoh: user@domain.com"
                    } else {
                        sharedPreferences.edit().putString("email", email).apply()
                        setToast("Akun $email Berhasil Terdaftar ")
                        requireView().findNavController()
                            .navigate(R.id.action_inputEmailFragment_to_inputPhoneNumberFragment)

                    }

                }

            }
        }

        binding.tiedtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (!s.toString().matches(emailPattern.toRegex())) {
                    binding.tiedtEmail.error = "Format email tidak sesuai. Contoh: user@domain.com"
                } else {
                    binding.tiedtEmail.error = null
                }
            }
        })
    }

    private fun setToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupAccessibility() {
        binding.apply {
            btnBack.contentDescription = getString(R.string.tombol_kembali)
            tvEmail.contentDescription = getString(R.string.email)
            tiedtEmail.contentDescription = getString(R.string.input_email)
            btnNext.contentDescription = getString(R.string.tombol_lanjut)
        }
    }
}