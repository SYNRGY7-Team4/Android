package com.synrgy7team4.feature_transfer.ui

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferSuccessBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
class TransferSuccessFragment : Fragment() {
    private var _binding: FragmentTransferSuccessBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransferViewModel by viewModel()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTransferSuccessBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)


        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            val id = sharedPreferences.getString("lastidtransaction", "") ?: ""
            viewModel.getMutation(id)
        }

        binding.btnClose.setOnClickListener {
            // val screenshotPath = takeScreenshot(binding.root)
            val bitmap = getBitmapFromView(binding.root)
            val path = saveBitmapToFile(bitmap)
            viewModel.saveScreenshotPath(path)
            requireView().findNavController().popBackStack()
        }

        viewModel.mutationData.observe(viewLifecycleOwner) { mutationData ->
            mutationData?.let {
                binding.layoutRecipient.tvName.text = mutationData.usernameTo
                binding.layoutRecipient.tvBankNum.text = mutationData.accountTo
                binding.layoutRecipient.tvBankName.text = "Lumi Bank"

                binding.layoutSender.tvSenderName.text = mutationData.usernameFrom
                binding.layoutSender.tvAccNum.text = mutationData.accountFrom

                binding.layoutTransDetail.tvTransTotal.text = "Rp. ${mutationData.amount}"
                binding.transStatus.tvTransDate.text = formatDateTime(mutationData.datetime)
                binding.transStatus.tvTransTime.text = formatHourTime(mutationData.datetime)

            }
        }
    }

    private fun formatDateTime(datetime: String?): String {
        val formatterWith6Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val formatterWith5Digits = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS")

        val localDateTime = try {
            LocalDateTime.parse(datetime, formatterWith6Digits)
        } catch (e: Exception) {
            LocalDateTime.parse(datetime, formatterWith5Digits)
        }

        val outputFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("id", "ID"))
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

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap): String {
        val file = File(requireContext().cacheDir, "screenshot.png")
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return file.absolutePath
    }
}
