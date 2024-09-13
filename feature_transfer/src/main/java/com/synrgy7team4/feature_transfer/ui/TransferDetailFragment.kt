package com.synrgy7team4.feature_transfer.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.common.makeToast
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

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1001
    }

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

        checkPermission()

        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            val id = sharedPreferences.getString("lastidtransaction", "") ?: ""
            viewModel.getMutation(id)
        }
        val destinationBinding = TransSuccessRecipientBinding.bind(binding.recipient.root)
        val transactionSuccessBinding = TransSuccessDetailBinding.bind(binding.transDetail.root)



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


        binding.btnShare.setOnClickListener {

            val getSS = takeScreenshot(view)
            val file = store(getSS)
            if (file != null) {
                shareImage(file)
            } else {
                Toast.makeText(context, "Gagal menyimpan screenshot", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDone.setOnClickListener {
            findNavController().popBackStack(R.id.transferDetailFragment, false)
            findNavController().navigate(R.id.action_transferDetailFragment_to_savedAccountFragment)
        }



    }


    private fun takeScreenshot(view: View): Bitmap {
        val screenView = view.rootView
        screenView?.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(screenView.drawingCache)
        screenView?.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun store(bitmap: Bitmap, ): File? {

        val dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (dir == null) {
            Toast.makeText(context, "Gagal mengakses direktori", Toast.LENGTH_SHORT).show()
            return null
        }

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val fileName = "screenshot_${System.currentTimeMillis()}.png"
        val file = File(dir, fileName)

        try {
            val fileOutputStream = file.outputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return file


    }

    private fun shareImage(file: File) {
        val uri = FileProvider.getUriForFile(requireContext(), "com.synrgy7team4.bankingapps.provider", file)
//        val uri = Uri.fromFile(file)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Image")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Saya Berhasil Transfer Uang ke Rekening Kamu")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            val chooser = Intent.createChooser(shareIntent, "Share Image")
            val resInfoList = requireContext().packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(chooser)

        } catch (e: ActivityNotFoundException) {
            makeToast(requireContext(), "No application found to share")
        }

    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
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