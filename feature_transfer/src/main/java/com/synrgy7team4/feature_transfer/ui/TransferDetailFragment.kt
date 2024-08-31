package com.synrgy7team4.feature_transfer.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferDetailBinding
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

@RequiresApi(Build.VERSION_CODES.O)
class TransferDetailFragment : Fragment() {
    private var _binding: FragmentTransferDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TransferViewModel>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTransferDetailBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            val id = sharedPreferences.getString("lastidtransaction", "") ?: ""
            viewModel.getMutation(id)
        }
        val destinationBinding = TransSuccessRecipientBinding.bind(binding.recipient.root)
        val transactionSuccessBinding = TransSuccessDetailBinding.bind(binding.transDetail.root)

        viewModel.screenshotPath.observe(viewLifecycleOwner) { path ->

        }

        viewModel.mutationData.observe(viewLifecycleOwner) { mutationData ->
            destinationBinding.tvRecipientName.text = mutationData.usernameTo
            destinationBinding.tvAccNum.text = mutationData.accountTo
            destinationBinding.tvBank.text = "Lumi Bank"

            binding.tvNominal.text = formatRupiah(mutationData.amount.toString())

            transactionSuccessBinding.transDate.text = formatDateTime(mutationData.datetime)
            transactionSuccessBinding.transTime.text = formatHourTime(mutationData.datetime)
        }

        binding.btnDetail.setOnClickListener {
            findNavController().navigate(R.id.action_transferDetailFragment_to_transferSuccessFragment)
        }

        binding.btnShare.setOnClickListener  {
            viewModel.screenshotPath.observe(viewLifecycleOwner) { path ->
                shareScreenshot(path)
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

        val dateTimePlus7Hours = localDateTime.plusHours(7)
        val outputFormatter = DateTimeFormatter.ofPattern("HH.mm", Locale("id", "ID"))
        return dateTimePlus7Hours.format(outputFormatter)
    }


}