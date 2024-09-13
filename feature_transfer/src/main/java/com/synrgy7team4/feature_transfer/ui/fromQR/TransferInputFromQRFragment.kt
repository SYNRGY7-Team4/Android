package com.synrgy7team4.feature_transfer.ui.fromQR

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferInputFromQRBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransferInputFromQRFragment : Fragment() {

    private var _binding: FragmentTransferInputFromQRBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TransferViewModel>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTransferInputFromQRBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)
        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            viewModel.getBalance()
            binding.userAccountName.text = viewModel.userName
            binding.userAccountNo.text = viewModel.accountNumber
        }



//-----------------------

//        val accountDestinationFromScan =
//            sharedPreferences.getString("accountDestinationFromScan", null)
//        val bankName = sharedPreferences.getString("bankname", "Lumi Bank")
//
//        val splitAccount = accountDestinationFromScan?.split(" ")
//        val accountNo = splitAccount?.get(0)
//        val username = splitAccount?.get(1)
//
//        sharedPreferences.edit().putString("accountDestinationNoFromQR", accountNo).apply()
//        sharedPreferences.edit().putString("accountDestinationNameFromQR", accountNo).apply()
//        binding.bankNameAndAccountNo.text = "$bankName - $accountNo"
//        binding.accountName.text = username

        val accountDestinationFromScan = sharedPreferences.getString("accountDestinationFromScan", null)
        accountDestinationFromScan?.let {
            val lines = it.split("\n")

            val nameLine = lines.getOrNull(0)
            val accountNumberLine = lines.getOrNull(1)

            // Extract the actual values
            val username = nameLine?.substringAfter("Name: ")?.trim()
            val accountNo = accountNumberLine?.substringAfter("Account Number: ")?.trim()


            binding.bankNameAndAccountNo.text = "Lumi Bank - $accountNo"
            sharedPreferences.edit().putString("accountDestinationNoFromQR", accountNo).apply()
            binding.accountName.text = username
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.submitForm.setOnClickListener { handleSubmitFormClick() }

        viewModel.balanceData.observe(viewLifecycleOwner) { balance ->
            balance?.let {
                binding.userAccountBalance.text = "Rp ${formatRupiah(it.data.toString())}"
            }
        }




    }



    private fun handleSubmitFormClick() {
        val nominal = binding.amountInputText.text.toString()
        val berita = binding.inputNote.text.toString()

        val isAmountValid = validateAmount(nominal)
        val isDescriptionValid = validateDescription(berita)

        if (isAmountValid && isDescriptionValid) {
            // If both validations pass, navigate to the next screen
            sharedPreferences.edit().putInt("transferAmountFromQR", nominal.toInt()).apply()
            sharedPreferences.edit().putString("transferDescriptionFromQr", berita).apply()
            findNavController().navigate(R.id.action_transferInputFromQRFragment_to_transferPinFromQRFragment)
        }



    }


    private fun validateDescription(description: String): Boolean {
        return if (description.isBlank()) {
            Toast.makeText(requireContext(), "Berita tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            false
        } else {
            binding.inputNote.error = null
            true
        }
    }

    private fun validateAmount(amount: String): Boolean {
        if (amount.isNullOrEmpty()) {
            binding.amountInputText.error = "Nominal harus di isi!"
            Toast.makeText(requireContext(), "Nominal harus di isi!", Toast.LENGTH_SHORT).show()
            return false
        }

        val nominal = amount.toIntOrNull() ?: 0

        return when {
            nominal == 0 -> {
                binding.amountInputText.error = "Nominal harus di isi!"
                Toast.makeText(requireContext(), "Nominal harus di isi!", Toast.LENGTH_SHORT).show()
                false
            }

            nominal < 1000 -> {
                binding.amountInputText.error = "Nominal harus di atas Rp. 1.000!"
                Toast.makeText(
                    requireContext(),
                    "Nominal harus di atas Rp. 1.000!",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            nominal > 5000000 -> {
                binding.amountInputText.error = "Nominal maksimal Rp. 5.000.000!"
                Toast.makeText(
                    requireContext(),
                    "Nominal maksimal Rp. 5.000.000!",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }

            else -> true
        }
    }


}