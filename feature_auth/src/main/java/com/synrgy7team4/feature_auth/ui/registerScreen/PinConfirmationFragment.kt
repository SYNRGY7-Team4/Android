package com.synrgy7team4.feature_auth.ui.registerScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.databinding.PinInputBinding
import com.synrgy7team4.common.databinding.PinNumberBinding
import com.synrgy7team4.common.makeToast
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentPinConfirmationBinding
import com.synrgy7team4.feature_auth.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinConfirmationFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentPinConfirmationBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModel<RegisterViewModel>()

    private lateinit var pinNumberBinding: PinNumberBinding
    private lateinit var pinInputBinding: PinInputBinding

    private val numberList = ArrayList<String>()
    private var passCode = ""
    private var input1: String? = null
    private var input2: String? = null
    private var input3: String? = null
    private var input4: String? = null
    private var input5: String? = null
    private var input6: String? = null
    private lateinit var firstPassCode: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPinConfirmationBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)

        pinNumberBinding = PinNumberBinding.bind(binding.pinNumber.root)
        pinInputBinding = PinInputBinding.bind(binding.pinInput.root)

        firstPassCode = sharedPreferences.getString("pin", "") ?: ""


        viewModel.registerResult.observe(viewLifecycleOwner) {result ->
            if (result != null) {
                if(result.success) {
                    makeToast(requireContext(), "Registrasi Berhasil: ${result.message}")
//                        findNavController().navigate(R.id.action_pinConfirmationFragment_to_registrationSuccessFragment)

                }
            }
        }
        initializeComponents()



        viewModel.error.observe(viewLifecycleOwner) { error ->
            makeToast(requireContext(), error.toString())
        }
//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            makeToast(requireContext(), error.message ?: "Unknown error occurred")
//        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun initializeComponents() {
        binding.apply {
            pinNumberBinding.btnNum1.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum2.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum3.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum4.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum5.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum6.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum7.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum8.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum9.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnNum0.setOnClickListener(this@PinConfirmationFragment)
            pinNumberBinding.btnDelete.setOnClickListener(this@PinConfirmationFragment)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            pinNumberBinding.btnNum1.id -> addNumberToList("1")
            pinNumberBinding.btnNum2.id -> addNumberToList("2")
            pinNumberBinding.btnNum3.id -> addNumberToList("3")
            pinNumberBinding.btnNum4.id -> addNumberToList("4")
            pinNumberBinding.btnNum5.id -> addNumberToList("5")
            pinNumberBinding.btnNum6.id -> addNumberToList("6")
            pinNumberBinding.btnNum7.id -> addNumberToList("7")
            pinNumberBinding.btnNum8.id -> addNumberToList("8")
            pinNumberBinding.btnNum9.id -> addNumberToList("9")
            pinNumberBinding.btnNum0.id -> addNumberToList("0")
            pinNumberBinding.btnDelete.id -> {
                if (numberList.isNotEmpty()) {
                    numberList.removeAt(numberList.size - 1)
                    passNumber(numberList)
                }
            }
        }
    }

    private fun addNumberToList(number: String) {
        numberList.add(number)
        passNumber(numberList)
    }

    private fun passNumber(numberList: ArrayList<String>) {
        clearPinDisplay()

        for (i in numberList.indices) {
            when (i) {
                0 -> {
                    input1 = numberList[0]
                    pinInputBinding.tvPinInput1.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                }

                1 -> {
                    input2 = numberList[1]
                    pinInputBinding.tvPinInput2.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                }

                2 -> {
                    input3 = numberList[2]
                    pinInputBinding.tvPinInput3.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                }

                3 -> {
                    input4 = numberList[3]
                    pinInputBinding.tvPinInput4.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                }

                4 -> {
                    input5 = numberList[4]
                    pinInputBinding.tvPinInput5.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                }

                5 -> {
                    input6 = numberList[5]
                    pinInputBinding.tvPinInput6.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
                    passCode = input1 + input2 + input3 + input4 + input5 + input6
                    if (passCode.length == 6) {
                        validatePin()
                    }
                }
            }
        }
    }

    private fun sendRegisterRequest() {
        val email = sharedPreferences.getString("email", "budi@example.com")
        val hp = sharedPreferences.getString("hp", "911")
        val password = sharedPreferences.getString("password", "12345678")
        val nik = sharedPreferences.getString("nik", "")
        val name = sharedPreferences.getString("name", "Budi")
        val date = sharedPreferences.getString("date", "01-01-2000")
//        val pin = sharedPreferences.getString("pin", "111111")
        val ktp = sharedPreferences.getString("ktp", "")
        val otp = sharedPreferences.getString("otp", "")
//        val isVerified = sharedPreferences.getBoolean("isverified", true)

        if (!email.isNullOrEmpty() &&
            !hp.isNullOrEmpty() &&
            !password.isNullOrEmpty() &&
            !nik.isNullOrEmpty() &&
            !name.isNullOrEmpty() &&
            !date.isNullOrEmpty() &&
//            !pin.isNullOrEmpty() &&
            !ktp.isNullOrEmpty() &&
            !otp.isNullOrEmpty()
        ) {
            viewModel.register(
                email = email,
                hp = hp,
                password = password,
                nik = nik,
                name = name,
                date = date,
                pin = firstPassCode,
//                pin = pin,
                ektp_photo = ktp,
                otp = otp,
                is_verified = true
            )
        } else {
            makeToast(requireContext(), "Anda harus mengulang proses registrasi")
        }
    }

    private fun validatePin() {
        if (firstPassCode == passCode) {


            sendRegisterRequest()
            findNavController().navigate(R.id.action_pinConfirmationFragment_to_registrationSuccessFragment)
        } else {
            makeToast(requireContext(), "PIN mismatch!")
            clearPinDisplay()
            numberList.clear()
        }
    }

    private fun clearPinDisplay() {
        pinInputBinding.tvPinInput1.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
        pinInputBinding.tvPinInput2.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
        pinInputBinding.tvPinInput3.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
        pinInputBinding.tvPinInput4.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
        pinInputBinding.tvPinInput5.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
        pinInputBinding.tvPinInput6.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//
//class PinConfirmationFragment : Fragment(), View.OnClickListener {
//
//    private var _binding: FragmentPinConfirmationBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var sharedPreferences: SharedPreferences
//
//    private lateinit var pinNumberBinding : PinNumberBinding
//    private lateinit var pinInputBinding : PinInputBinding
//
//
//    private val viewModel by viewModels<RegisterViewModel> {
////        val app = requireActivity().application
////        (app as MyApplication).viewModelFactory
//
//        val app = requireActivity().application as ViewModelFactoryProvider
//        app.provideViewModelFactory()
//    }
//
//    private val numberList = ArrayList<String>()
//    private var passCode = ""
//    private var input1: String? = null
//    private var input2: String? = null
//    private var input3: String? = null
//    private var input4: String? = null
//    private var input5: String? = null
//    private var input6: String? = null
//    private lateinit var firstPassCode: String
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentPinConfirmationBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        sharedPreferences = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)
//
//        pinNumberBinding = PinNumberBinding.bind(binding.pinNumber.root)
//        pinInputBinding = PinInputBinding.bind(binding.pinInput.root)
//
//        firstPassCode = arguments?.getString("passCode") ?: ""
//        initializeComponents()
//
////        viewModel.error.observe(viewLifecycleOwner) {error->
////            setToast(error.toString())
////        }
////        viewModel.registerResult.observe(viewLifecycleOwner) { result ->
////            if (result != null) {
////                setToast("Registrasi Berhasil: $result")
////                viewModel.clearRegisterResult()
////            }
////
//////                result?.let {
//////                    viewModel.clearRegisterResult()
//////                }
////        }
//
////        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
////            if (errorMessage != null) {
////                setToast("Registrasi Gagal: $errorMessage")
////                viewModel.clearError()
////            }
////
//////                errorMessage?.let {
//////                    viewModel.clearError()
//////                }
////        }
//    }
//
//    private fun initializeComponents() {
//        binding.apply {
//            pinNumberBinding.btnNum1.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum2.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum3.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding. btnNum4.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum5.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum6.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum7.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnNum8.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding. btnNum9.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding. btnNum0.setOnClickListener(this@PinConfirmationFragment)
//            pinNumberBinding.btnDelete.setOnClickListener(this@PinConfirmationFragment)
//        }
//    }
//
//    override fun onClick(view: View) {
//        when (view.id) {
//            pinNumberBinding.btnNum1.id-> addNumberToList("1")
//            pinNumberBinding.btnNum2.id -> addNumberToList("2")
//            pinNumberBinding.btnNum3.id -> addNumberToList("3")
//            pinNumberBinding.btnNum4.id -> addNumberToList("4")
//            pinNumberBinding.btnNum5.id -> addNumberToList("5")
//            pinNumberBinding.btnNum6.id -> addNumberToList("6")
//            pinNumberBinding.btnNum7.id -> addNumberToList("7")
//            pinNumberBinding.btnNum8.id -> addNumberToList("8")
//            pinNumberBinding.btnNum9.id -> addNumberToList("9")
//            pinNumberBinding.btnDelete.id  -> {
//                if (numberList.isNotEmpty()) {
//                    numberList.removeAt(numberList.size - 1)
//                    passNumber(numberList)
//                }
//            }
//        }
//    }
//
//    private fun addNumberToList(number: String) {
//        numberList.add(number)
//        passNumber(numberList)
//    }
//
//    private fun passNumber(numberList: ArrayList<String>) {
//        clearPinDisplay()
//
//        for (i in numberList.indices) {
//            when (i) {
//                0 -> {
//                    input1 = numberList[0]
//                    pinInputBinding.tvPinInput1.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                }
//                1 -> {
//                    input2 = numberList[1]
//                    pinInputBinding.tvPinInput2.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                }
//                2 -> {
//                    input3 = numberList[2]
//                    pinInputBinding.tvPinInput3.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                }
//                3 -> {
//                    input4 = numberList[3]
//                    pinInputBinding.tvPinInput4.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                }
//                4 -> {
//                    input5 = numberList[4]
//                    pinInputBinding.tvPinInput5.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                }
//                5 -> {
//                    input6 = numberList[5]
//                    pinInputBinding.tvPinInput6.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet_filled)
//                    passCode = input1 + input2 + input3 + input4 + input5 + input6
//                    if (passCode.length == 6) {
//                        matchPassCode()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun clearPinDisplay() {
//        binding.apply {
//            pinInputBinding.tvPinInput1.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//            pinInputBinding.tvPinInput2.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//            pinInputBinding.tvPinInput3.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//            pinInputBinding. tvPinInput4.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//            pinInputBinding.tvPinInput5.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//            pinInputBinding.tvPinInput6.setBackgroundResource(com.synrgy7team4.common.R.drawable.pin_bullet)
//        }
//    }
//
//    private fun matchPassCode() {
//        val deepLinkUri = Uri.parse("app://com.example.app/auth/registrationSuccess" )
//
//        if (firstPassCode == passCode) {
//
////            sharedPreferences.edit().putString("confirm_pin", passCode).apply()
////            sendRegisterRequest()
////            setToast("Selamat! Registrasi Berhasil, \nTerimakasih Telah Melengkapi Data Kamu ")
//            setToast("Kamu Berhasil Membuat Pin ")
////            requireView().findNavController().navigate(R.id.action_pinConfirmationFragment_to_registrationSuccessFragment)
//            requireView().findNavController().navigate(deepLinkUri)
//
//            //requireView().findNavController().navigate(R.id.action_pinConfirmationFragment_to_uploadKtpFragment)
//        } else {
//            Toast.makeText(requireContext(), "Password Tidak Cocok", Toast.LENGTH_SHORT).show()
//            numberList.clear()
//            clearPinDisplay()
//        }
//    }
//
////    private fun sendRegisterRequest() {
////        val email = sharedPreferences.getString("email", "budi@example.com")
////        val hp = sharedPreferences.getString("hp", "911")
////        val password = sharedPreferences.getString("password", "12345678")
////        val confirm_password = sharedPreferences.getString("confirm_password", "12345678")
////        val ktp = sharedPreferences.getString("ktp", "")
////        val name = sharedPreferences.getString("name", "Budi")
////        val date = sharedPreferences.getString("date", "01-01-2000")
////        val pin = sharedPreferences.getString("pin", "111111")
////        val confirm_pin = sharedPreferences.getString("confirm_pin", "111111")
////
////        if (!email.isNullOrEmpty() &&
////            !hp.isNullOrEmpty() &&
////            !password.isNullOrEmpty() &&
////            !confirm_password.isNullOrEmpty() &&
////            !ktp.isNullOrEmpty() &&
////            !name.isNullOrEmpty() &&
////            !date.isNullOrEmpty() &&
////            !pin.isNullOrEmpty() &&
////            !confirm_pin.isNullOrEmpty()
////            )
////        {
////
////            viewModel.registerUser(email, hp, password, ktp, name, date, pin)
////
////        } else {
////            setToast("Registration data is not complete")
////        }
////
////    }
//
//    private fun setToast(msg: String) {
//        Toast.makeText(requireActivity(),msg, Toast.LENGTH_SHORT).show()
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
