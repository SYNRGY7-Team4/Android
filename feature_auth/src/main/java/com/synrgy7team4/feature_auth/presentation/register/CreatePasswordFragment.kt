package com.synrgy7team4.feature_auth.presentation.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jer.shared.ViewModelFactoryProvider
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentCreatePasswordBinding
import com.synrgy7team4.feature_auth.presentation.viewmodel.RegisterViewModel


class CreatePasswordFragment : Fragment() {



    private var _binding: FragmentCreatePasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModels<RegisterViewModel> {
//        val app = requireActivity().application
//        (app as MyApplication).viewModelFactory

        val app = requireActivity().application as ViewModelFactoryProvider
        app.provideViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)


        binding.submitCreatedPassword.setOnClickListener {
            // Navigate with a delay
            val password = binding.inputPassword.text.toString()
            val passwordConfirmation = binding.inputPasswordConfirmation.text.toString()

            when {
                password.isEmpty() -> binding.inputPassword.error = "Password tidak boleh kosong"
                passwordConfirmation.isEmpty() -> binding.inputPasswordConfirmation.error = "Password tidak boleh kosong"
                else -> {
                    if (password.contains(Regex("[^a-zA-Z0-9]"))) {
                        Snackbar.make(view, "Password tidak boleh mengandung simbol", Snackbar.LENGTH_SHORT).show()
                    } else if (password.length < 8) {
                        Snackbar.make(view, "Password harus terdiri dari 8-15 karakter", Snackbar.LENGTH_SHORT).show()
                    } else if (password.length > 15) {
                        Snackbar.make(view, "Password harus terdiri dari 8-15 karakter", Snackbar.LENGTH_SHORT).show()
                    } else {
                        if (passwordConfirmation != password) {
                            Snackbar.make(view, "Password tidak sama, mohon input kembali", Snackbar.LENGTH_SHORT).show()

                        } else {
                            sharedPreferences.edit().putString("password", password).apply()
//                            sharedPreferences.edit().putString("confirm_password", passwordConfirmation).apply()
                            setToast("Kamu berhasil membuat password")
                            view.findNavController().navigate(R.id.action_createPasswordFragment_to_biodataFragment)
                        }
                    }




                }
            }

            binding.inputPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val pw = s.toString()

                    if (pw.contains(Regex("[^a-zA-Z0-9]"))) {
                        Snackbar.make(view, "Password tidak boleh mengandung simbol", Snackbar.LENGTH_SHORT).show()
                    } else if (pw.length < 8) {
                        Snackbar.make(view, "Password harus terdiri dari 8-15 karakter", Snackbar.LENGTH_SHORT).show()
                    } else if (pw.length > 15) {
                        Snackbar.make(view, "Password harus terdiri dari 8-15 karakter", Snackbar.LENGTH_SHORT).show()
                    } else {
                        binding.inputPassword.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

//
//            view.postDelayed({
//
//            }, 2000) // Delay 2 seconds
        }

//        binding.submitCreatedPassword.setOnClickListener { getPassword() }
    }

    private fun setToast(msg: String) {
        Toast.makeText(requireActivity(),msg, Toast.LENGTH_SHORT).show()

    }



//    private fun getPassword() {
//        val password = binding.inputPassword.text
//        val passwordConfirmation = binding.inputPasswordConfirmation.text
//
//        Log.d("password", password.toString())
//        Log.d("passwordConfirmation", passwordConfirmation.toString())
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}