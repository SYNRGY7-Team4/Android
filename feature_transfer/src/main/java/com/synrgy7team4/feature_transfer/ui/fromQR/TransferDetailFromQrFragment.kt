package com.synrgy7team4.feature_transfer.ui.fromQR

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferDetailBinding
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferDetailFromQrBinding
import com.synrgy7team4.feature_transfer.databinding.TransSuccessDetailBinding
import com.synrgy7team4.feature_transfer.databinding.TransSuccessRecipientBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class TransferDetailFromQrFragment : Fragment() {

    private var _binding: FragmentTransferDetailFromQrBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TransferViewModel>()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentTransferDetailFromQrBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            val id = sharedPreferences.getString("lastidtransactionFromQr", "") ?: ""
            viewModel.getMutation(id)
        }
        val destinationBinding = TransSuccessRecipientBinding.bind(binding.recipient.root)
        val transactionSuccessBinding = TransSuccessDetailBinding.bind(binding.transDetail.root)

        viewModel.screenshotPath.observe(viewLifecycleOwner) { path ->
            if (path != null) {

                shareScreenshot(path)
            }
        }


        viewModel.mutationData.observe(viewLifecycleOwner) { mutationData ->
            destinationBinding.tvRecipientName.text = mutationData.usernameTo
            destinationBinding.tvAccNum.text = mutationData.accountTo
            destinationBinding.tvBank.text = "Lumi Bank"

//            binding.tvNominal.text = mutationData.amount.toString()
            binding.tvNominal.text = formatRupiah(mutationData.amount.toString())


            transactionSuccessBinding.transDate.text = formatDateTime(mutationData.datetime)
            transactionSuccessBinding.transTime.text = formatHourTime(mutationData.datetime)
//            transactionSuccessBinding.transDate.text = formatRupiah(mutationData.datetime)
//            transactionSuccessBinding.transTime.text = formatHourTime(mutationData.datetime)
        }

        binding.btnDetail.setOnClickListener {
            findNavController().navigate(R.id.action_transferDetailFromQrFragment_to_transferSuccessFromQrFragment)
        }

        binding.btnShare.setOnClickListener {
            val screenshotPath = viewModel.screenshotPath.value
            if (screenshotPath != null) {
                if (File(screenshotPath).exists()) {
                    shareScreenshot(screenshotPath)
                } else {
                    Toast.makeText(
                        context,
                        "File tangkapan layar tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "Tangkapan layar belum tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack(R.id.savedAccountFragment, false)

        }

    }

    private fun shareScreenshot(path: String) {
        val file = File(path)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(shareIntent, "Bagikan tangkapan layar melalui"))
    }


    private fun formatDateTime(datetime: String?): String {
        val formatterWith6Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val formatterWith5Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")

        val localDateTime = try {
            LocalDateTime.parse(datetime, formatterWith6Digits)
        } catch (e: Exception) {
            LocalDateTime.parse(datetime, formatterWith5Digits)
        }

        val outputFormatter =
            DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return localDateTime.format(outputFormatter)
    }

    private fun formatHourTime(datetime: String?): String {
        val formatterWith6Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val formatterWith5Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")

        val localDateTime = try {
            LocalDateTime.parse(datetime, formatterWith6Digits)
        } catch (e: Exception) {
            LocalDateTime.parse(datetime, formatterWith5Digits)
        }

        val outputFormatter = DateTimeFormatter.ofPattern("HH.mm", Locale("id", "ID"))
        return localDateTime.format(outputFormatter)
    }

}


