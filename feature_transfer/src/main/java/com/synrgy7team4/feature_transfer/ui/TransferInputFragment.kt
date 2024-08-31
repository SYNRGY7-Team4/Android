package com.synrgy7team4.feature_transfer.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferInputBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransferInputFragment : Fragment() {
    private var _binding: FragmentTransferInputBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TransferViewModel>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTransferInputBinding.inflate(layoutInflater).also {
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


        val accountDestinationName = sharedPreferences.getString("accountDestinationName", null)
        val accountDestinationNo = sharedPreferences.getString("accountDestinationNo", null)
        val bankName = sharedPreferences.getString("bankname", "Lumi Bank")

        binding.bankNameAndAccountNo.text = "Lumi - $accountDestinationNo"
        binding.accountName.text = accountDestinationName ?: "Rekening Tujuan Baru"

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
            sharedPreferences.edit().putInt("transferAmount", nominal.toInt()).apply()
            sharedPreferences.edit().putString("transferDescription", berita).apply()
            findNavController().navigate(R.id.action_transferInputFragment_to_transferPinFragment)
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
        if (amount.isBlank()) {
            Toast.makeText(requireContext(), "Nominal harus di isi!", Toast.LENGTH_SHORT).show()
            return false
        }

        val nominal = amount.toIntOrNull() ?: 0

        return when {
            nominal == 0 -> {
                Toast.makeText(requireContext(), "Nominal harus di isi!", Toast.LENGTH_SHORT).show()
                false
            }
            nominal < 1000 -> {
                Toast.makeText(requireContext(), "Nominal harus di atas Rp. 1.000!", Toast.LENGTH_SHORT).show()
                false
            }
            nominal > 5000000 -> {
                Toast.makeText(requireContext(), "Nominal maksimal Rp. 25.000.000!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                binding.amountInputText.error = null
                true
            }
        }
    }


}

