package com.synrgy7team4.feature_transfer.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.synrgy7team4.common.makeToast
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentCheckReceiverDetailBinding
import com.synrgy7team4.feature_transfer.databinding.FragmentFellowAccountBankInputBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FellowAccountBankInputFragment : Fragment() {
    private var _binding: FragmentFellowAccountBankInputBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel by viewModel<TransferViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFellowAccountBankInputBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)

        viewModel.accountAllList.observe(viewLifecycleOwner) { accounts ->
            val bankname = sharedPreferences.getString("bankname", "") ?: ""
            if (accounts == null) {
                makeToast(requireContext(), "Account not found")
            } else {
                showDialogReceiverAccountCheck(accounts.userName, accounts.accountNumber)
                sharedPreferences.edit().apply {
                    putString("accountDestinationName", accounts.userName)
                    putString("accountDestinationNo", accounts.accountNumber)
                    apply()
                }
            }
        }

        viewModel.accountSaveResponse.observe(viewLifecycleOwner) {
            if (it.success) {
                findNavController().navigate(R.id.action_fellowAccountBankInputFragment_to_transferInputFragment)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e("YourTag", "Error: ${error.message}")
            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }


        val spinner = binding.bankDropdown
        val id = sharedPreferences.getString("differentbank", "")
        if (id=="true") {
            val spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.bank_list, // Use your string-array resource
                android.R.layout.simple_spinner_item
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Get the selected item
                    val selectedBank = parent.getItemAtPosition(position).toString()

                    // Save the selected item to SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("bankname", selectedBank)
                        apply()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Optionally handle cases where no selection is made
                }
            }
        } else {
            spinner.visibility = View.GONE
            binding.tvAlert.visibility = View.GONE
            binding.tvBankName.visibility = View.GONE
        }


        binding.btnBack.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.btnNext.setOnClickListener {
            val etUserName = binding.tiedtNomorRekening as EditText
            val strUserName = etUserName.text.toString()
            if (TextUtils.isEmpty(strUserName)) {
                etUserName.error = "Nomor Rekening tidak boleh kosong!"
                return@setOnClickListener
            } else {
                lifecycleScope.launch {
                    awaitAll(viewModel.initializeData())
                    viewModel.cekAccountList(strUserName)
                }
            }
        }
    }
//
    private fun showDialogReceiverAccountCheck(userName: String?, accountNumber: String?) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_check_receiver_detail)
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = com.synrgy7team4.common.R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val btnClose = dialog.findViewById<ImageView>(R.id.btn_close)
        val tvAccountNameReceiver = dialog.findViewById<TextView>(R.id.tv_account_name_receiver)
        val tvBankNameAccountBankReceiver = dialog.findViewById<TextView>(R.id.tv_bank_name_account_bank_receiver)
        val btnNext = dialog.findViewById<TextView>(R.id.btn_next)
        val btnSaveReceiver = dialog.findViewById<TextView>(R.id.btn_save_receiver)

    val selectedBank = sharedPreferences.getString("bankname","Lumi")
    tvAccountNameReceiver.text = userName
   // tvBankNameAccountBankReceiver.text = "${selectedBank} - ${accountNumber}"

        tvAccountNameReceiver.text = userName
        tvBankNameAccountBankReceiver.text = "Lumi Bank - ${accountNumber}"

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnNext.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_fellowAccountBankInputFragment_to_transferInputFragment)
        }

        btnSaveReceiver.setOnClickListener {
            viewModel.saveAccount(binding.tiedtNomorRekening.text.toString())
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}