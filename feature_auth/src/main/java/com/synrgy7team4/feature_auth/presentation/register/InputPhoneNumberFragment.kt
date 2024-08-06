package com.synrgy7team4.feature_auth.presentation.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jer.shared.ViewModelFactoryProvider
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentInputPhoneNumberBinding
import com.synrgy7team4.feature_auth.presentation.viewmodel.RegisterViewModel

class InputPhoneNumberFragment : Fragment() {
    private val binding by lazy { FragmentInputPhoneNumberBinding.inflate(layoutInflater) }
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<RegisterViewModel> {
//        val app = requireActivity().application
//        (app as MyApplication).viewModelFactory

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
            val hp = binding.tiedtPhoneNumber.text.toString()
            when {
                hp.isEmpty() -> binding.tiedtPhoneNumber.error = "No Hp Tidak Boleh Kodong"
                else -> {
                    if (hp[0] != '8') {
                        binding.tiedtPhoneNumber.error = "Nomor HP harus diawali dengan angka 8"
                    } else if (hp.length < 11) {
                        binding.tiedtPhoneNumber.error = "Nomor HP harus berjumlah 11-13 digit"
                    } else if (hp.length > 13) {
                        binding.tiedtPhoneNumber.error = "Nomor HP harus berjumlah 11-13 digit"
                    } else {
                        sharedPreferences.edit().putString("hp", hp).apply()
                        setToast("Nomor $hp Kamu Berhasil Ditambahkan")
                        view.findNavController()
                            .navigate(R.id.action_inputPhoneNumberFragment_to_otpVerification)
                    }

//                    sharedPreferences.edit().putString("hp", hp).apply()
//                    setToast("Nomor $hp Kamu Berhasil Ditambahkan")
//
//                    view.findNavController().navigate(R.id.action_inputPhoneNumberFragment_to_otpVerification)
                }
            }

        }

        binding.tiedtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val phoneNumber = s.toString()
                if (phoneNumber.isNotEmpty() && phoneNumber[0] != '8') {
                    binding.tiedtPhoneNumber.error = "Nomor HP harus diawali dengan angka 8"
                } else if (phoneNumber.length < 11) {
                    binding.tiedtPhoneNumber.error = "Nomor HP harus berjumlah 11-13 digit"
                } else if (phoneNumber.length > 13) {
                    binding.tiedtPhoneNumber.error = "Nomor HP harus berjumlah 11-13 digit"
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    private fun setToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupAccessibility() {
        binding.apply {
            btnBack.contentDescription = getString(R.string.tombol_kembali)
            tvPhoneNumber.contentDescription = getString(R.string.no_hp)
            tiedtPhoneNumber.contentDescription = getString(R.string.input_nomor_hp)
            btnNext.contentDescription = getString(R.string.tombol_lanjut)
        }
    }
}