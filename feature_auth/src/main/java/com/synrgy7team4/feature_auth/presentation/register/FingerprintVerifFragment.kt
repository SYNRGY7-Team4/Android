package com.synrgy7team4.feature_auth.presentation.register

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentFingerprintVerifBinding
import java.util.concurrent.Executor

class FingerprintVerifFragment : Fragment() {
    private var _binding: FragmentFingerprintVerifBinding? = null
    private val binding get() = _binding!!
    lateinit var info: String

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFingerprintVerifBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//         executor = ContextCompat.getMainExecutor(requireContext())
//         biometricPrompt = BiometricPrompt(this, executor,
//             object : BiometricPrompt.AuthenticationCallback() {
//                 override fun onAuthenticationError(
//                     errorCode: Int,
//                     errString: CharSequence,
//                 ) {
//                     super.onAuthenticationError(errorCode, errString)
//                     Toast.makeText(requireContext(),
//                         "Authentication error: $errString", Toast.LENGTH_SHORT)
//                         .show()
//                 }

//                 override fun onAuthenticationSucceeded(
//                     result: BiometricPrompt.AuthenticationResult,
//                 ) {
//                     super.onAuthenticationSucceeded(result)
//                     // DISINI KALO SUCCES BIOMETRICNYA, bisa kasih nav/intent disini
//                     requireView().findNavController().navigate(R.id.action_fingerprintVerifFragment_to_pinFragment)
//                     Toast.makeText(requireContext(),
//                         "Authentication succeeded!", Toast.LENGTH_SHORT)
//                         .show()
//                 }

//                 override fun onAuthenticationFailed() {
//                     super.onAuthenticationFailed()
//                     Toast.makeText(requireContext(), "Authentication failed",
//                         Toast.LENGTH_SHORT)
//                         .show()
//                 }
//             })

//        checkDeviceHasBiometric()
        binding.ivFingerprint.setOnClickListener {
//            biometricPrompt.authenticate(promptInfo)
            //requireView().findNavController().navigate(R.id.action_fingerprintVerifFragment_to_pinFragment)

        }

//         binding.ivFingerprint.setOnClickListener {
//             view.findNavController().navigate(R.id.action_fingerprintVerifFragment_to_pinFragment)
//        }
//        executor = ContextCompat.getMainExecutor(requireContext())
//        biometricPrompt = BiometricPrompt(this, executor,
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationError(
//                    errorCode: Int,
//                    errString: CharSequence,
//                ) {
//                    super.onAuthenticationError(errorCode, errString)
//                    Toast.makeText(requireContext(),
//                        "Authentication error: $errString", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//                override fun onAuthenticationSucceeded(
//                    result: BiometricPrompt.AuthenticationResult,
//                ) {
//                    super.onAuthenticationSucceeded(result)
//                    // DISINI KALO SUCCES BIOMETRICNYA, bisa kasih nav/intent disini
//                    requireView().findNavController().navigate(R.id.action_fingerprintVerifFragment_to_pinFragment)
//                    Toast.makeText(requireContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT)
//                        .show()
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    Toast.makeText(requireContext(), "Authentication failed",
//                        Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//
//        checkDeviceHasBiometric()
//        binding.ivFingerprint.setOnClickListener {
//            biometricPrompt.authenticate(promptInfo)
//        }

//
//        promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric login for Register")
//            .setSubtitle("Log in using your biometric credential")
//            .setNegativeButtonText("Use account password")
//            .build()



    }

    fun checkDeviceHasBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                info = "Sentuh Sensor Fingerprint"


            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
                info = "Tidak ada Sensor Fingerprint di device ini"


            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                info = "Sensor FIngerprint sedang tidak bisa digunakan"

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }

                startActivityForResult(enrollIntent, 100)
            }
        }
        binding.tvDescFingerprint.text = info
    }
}

//
//
//    }
//
//    fun checkDeviceHasBiometric() {
//        val biometricManager = BiometricManager.from(requireContext())
//        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
//            BiometricManager.BIOMETRIC_SUCCESS -> {
//                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
//                info = "Sentuh Sensor Fingerprint"
//
//
//            }
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
//                Log.e("MY_APP_TAG", "No biometric features available on this device.")
//                info = "Tidak ada Sensor Fingerprint di device ini"
//
//
//            }
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
//                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
//                info = "Sensor FIngerprint sedang tidak bisa digunakan"
//
//            }
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                // Prompts the user to create credentials that your app accepts.
//                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                    putExtra(
//                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
//                    )
//                }
//
//                startActivityForResult(enrollIntent, 100)
//            }
//        }
//        binding.tvDescFingerprint.text = info
//    }


