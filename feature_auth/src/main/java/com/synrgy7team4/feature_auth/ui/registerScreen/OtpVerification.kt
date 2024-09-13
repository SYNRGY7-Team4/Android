package com.synrgy7team4.feature_auth.ui.registerScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.makeToast
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentOtpVerificationBinding
import com.synrgy7team4.feature_auth.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpVerification : Fragment() {
    private var _binding: FragmentOtpVerificationBinding? = null
    private val binding get() = _binding!!

    private var isForgotPassword: Boolean = false
    private var countDownTimer: CountDownTimer? = null
    private var isResendEnabled = false


    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModel<RegisterViewModel>()

    private lateinit var inputCode1: EditText
    private lateinit var inputCode2: EditText
    private lateinit var inputCode3: EditText
    private lateinit var inputCode4: EditText
    private lateinit var inputCode5: EditText
    private lateinit var inputCode6: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentOtpVerificationBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputCode1 = view.findViewById(R.id.inputCode1)
        inputCode2 = view.findViewById(R.id.inputCode2)
        inputCode3 = view.findViewById(R.id.inputCode3)
        inputCode4 = view.findViewById(R.id.inputCode4)
        inputCode5 = view.findViewById(R.id.inputCode5)
        inputCode6 = view.findViewById(R.id.inputCode6)


        sharedPreferences =
            requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)
//        isForgotPassword = sharedPreferences.getBoolean("isForgotPassword", false)

        val email = sharedPreferences.getString("email", "user@example.com")
        val hp = sharedPreferences.getString("hp", "88888888111")
        //binding.tvNumber.text = email
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

//        binding.btnResendOtp.setOnClickListener {
//            if (isResendEnabled) {
//                resendOtp()
//                startCountdownTimer()
//            }
//        }

        startCountdownTimer()

        binding.submitOTPButton.setOnClickListener {
            val otp = getOTP()

            if (isForgotPassword) {
                email?.let { it1 -> viewModel.validateForgetPass(it1, otp) }
            } else {
                email?.let { it1 -> viewModel.verifyOtp(it1, otp) }
            }
//            val otp = getOTP()
//            val email = sharedPreferences.getString("email", "")
//            if (isForgotPassword) {
//                email?.let { it1 -> viewModel.validateForgetPass(it1, otp) }
//            } else {
//                email?.let { it1 -> viewModel.verifyOtp(it1, otp) }
//            }

//            view.findNavController().navigate(R.id.action_otpVerification_to_createPasswordFragment)
        }



        binding.btnResendOtp.setOnClickListener {
            if (isForgotPassword) {
                email?.let { it1 -> viewModel.sendForgetPass(it1) }
            } else {
                viewModel.sendOtp(email.toString(), hp.toString())
            }
        }

        viewModel.verifyOtpResult.observe(viewLifecycleOwner) { otpResponse ->
            val otp = getOTP()
            if (otpResponse.success) {
                sharedPreferences.edit().putString("otp", otp).apply()
                view.findNavController().navigate(R.id.action_otpVerification_to_createPasswordFragment)
            } else {
                makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi.")
            }
        }

        viewModel.validateForgetPassResult.observe(viewLifecycleOwner) { result ->
            if (result.status) {
            sharedPreferences.edit().putString("otpForget", getOtpForget()).apply()
                makeToast(requireContext(), result.message)
                view.findNavController().navigate(R.id.action_otpVerification_to_createPasswordFragment)

            } else {
                //makeToast(requireContext(), result.message)
//                makeToast(requireContext(), "Kode OTP berhasil diverifikasi")
                makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi")

            }
        }

//        viewModel.sendForgetPassResult.observe(viewLifecycleOwner) { result ->
//            if (result.status) {
//                makeToast(requireContext(), "Gagal mengirim ulang OTP. Silakan coba lagi.")
//
//            } else {
//                makeToast(requireContext(), "OTP berhasil dikirim ulang")
//                startCountdownTimer()
//            }
////            sharedPreferences.edit().putString("otpForget", getOtpForget()).apply()
////            view.findNavController().navigate(R.id.action_otpVerification_to_createPasswordFragment)
////            if (result.status) {
////                makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi.")
////
////            } else {
////                makeToast(requireContext(), "Kode OTP berhasil diverifikasi")
////
////
////            }
//        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
           // makeToast(requireContext(), error.toString())
            makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi.")

        }



        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.sendOtpResult.observe(viewLifecycleOwner) { otpResponse ->
            if (otpResponse.success) {
                Log.d("OTP", otpResponse.message)
                makeToast(requireContext(), otpResponse.message)
//                makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi.")
            }
        }

        viewModel.sendForgetPassResult.observe(viewLifecycleOwner) { otpResponse ->
            if (otpResponse.status) {
                Log.d("OTP", otpResponse.message)
                makeToast(requireContext(), otpResponse.message)
            } else {
                makeToast(requireContext(), "OTP tidak valid. Silakan coba lagi.")
            }
        }

        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
        }

        setupOTPInputs()
    }
//
//    private fun resendOtp() {
//        val email = sharedPreferences.getString("email", "")
//        val hp = sharedPreferences.getString("hp", "")
//        if (isForgotPassword) {
//            email?.let { viewModel.sendForgetPass(it) }
//        } else {
//            viewModel.sendOtp(email.toString(), hp.toString())
//        }
//        startCountdownTimer()
//
//    }

    private fun startCountdownTimer() {
        isResendEnabled = true
//        binding.btnResendOtp.isEnabled = false

        btnResendActive(!isResendEnabled)

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.countDown.text = "00:${String.format("%02d", seconds)}"
            }

            override fun onFinish() {
                binding.countDown.text = "00:00"
                btnResendActive(isResendEnabled)
//                binding.btnResendOtp.isEnabled = true
            }
        }.start()
    }

    private fun getOtpForget(): String {
        return "${inputCode1.text}${inputCode2.text}${inputCode3.text}${inputCode4.text}${inputCode5.text}${inputCode6.text}"

    }

    private fun setupOTPInputs() {
        //when use input number
        addTextWatcher(inputCode1, inputCode2)
        addTextWatcher(inputCode2, inputCode3)
        addTextWatcher(inputCode3, inputCode4)
        addTextWatcher(inputCode4, inputCode5)
        addTextWatcher(inputCode5, inputCode6)

        //when use delete or erase
        inputCode1.setOnKeyListener(GenericKeyEvent(inputCode1, null))
        inputCode2.setOnKeyListener(GenericKeyEvent(inputCode2, inputCode1))
        inputCode3.setOnKeyListener(GenericKeyEvent(inputCode3, inputCode2))
        inputCode4.setOnKeyListener(GenericKeyEvent(inputCode4, inputCode3))
        inputCode5.setOnKeyListener(GenericKeyEvent(inputCode5, inputCode4))
        inputCode6.setOnKeyListener(GenericKeyEvent(inputCode6, inputCode5))
    }

    private fun addTextWatcher(inputCodeFirst: EditText, inputCodeSecond: EditText) {
        return inputCodeFirst.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    inputCodeSecond.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getOTP(): String {
        return "${inputCode1.text}${inputCode2.text}${inputCode3.text}${inputCode4.text}${inputCode5.text}${inputCode6.text}"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun btnResendActive(isActive: Boolean) {
        if (isActive) {
            binding.btnResendOtp.visibility = View.VISIBLE
        } else {
            binding.btnResendOtp.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel() // Batalkan timer saat view dihancurkan
        _binding = null
    }
}

class GenericKeyEvent internal constructor(
    private val currentView: EditText,
    private val previousView: EditText?
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.inputCode1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }
}