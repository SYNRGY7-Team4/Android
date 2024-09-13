package com.synrgy7team4.feature_dashboard.ui.qris

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import com.synrgy7team4.common.formatRupiah
import com.synrgy7team4.common.makeToast
import com.synrgy7team4.feature_dashboard.R
import com.synrgy7team4.feature_dashboard.databinding.FragmentQrisBinding
import com.synrgy7team4.feature_dashboard.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class QrisFragment : Fragment() {
    private var _binding: FragmentQrisBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<HomeViewModel>()
    private var isBalanceHidden: Boolean = true
    private var isOnFlash: Boolean = false
    private lateinit var cameraId: String
    private lateinit var username: String
    private lateinit var hiddenBalance: String
    private lateinit var userBalance: String
    private lateinit var hiddenAccNum: String
    private lateinit var accountNumber: String
    private lateinit var codeScanner: CodeScanner
    private lateinit var cameraManager: CameraManager
    private lateinit var sharedPreferences: SharedPreferences

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
                codeScanner.startPreview()
            } else {
                Toast.makeText(requireContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentQrisBinding.inflate(layoutInflater).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)

        viewModel.getUserData()

        cameraManager = getSystemService(requireContext(), CameraManager::class.java)!!

        codeScanner = CodeScanner(requireActivity(), binding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK

        codeScanner.decodeCallback = DecodeCallback { textResultScan ->
            requireActivity().runOnUiThread {
                makeToast(requireContext(), textResultScan.text)
                sharedPreferences.edit()
                    .putString("accountDestinationFromScan", textResultScan.text).apply()

                val deepLinkToTrans = Uri.parse("app://com.example.app/trans/transferInputFromQR")
                findNavController().navigate(deepLinkToTrans)

            }
        }
        checkCameraPermissionAndOpenCamera()

        binding.payButton.setOnClickListener {
            sharedPreferences.edit().putString("payOrReceived", "membayar").apply()
            showDialogQRGeneratePage()
        }

        binding.transferButton.setOnClickListener {
            sharedPreferences.edit().putString("payOrReceived", "menerima transfer").apply()
            showDialogQRGeneratePage()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.galleryButton.setOnClickListener {
            openGallery()
        }

        accountNumber = R.string.dummy_acc_number.toString()
        username = R.string.dummy_name.toString()

        viewModel.userData.observe(viewLifecycleOwner) { userData ->
            viewModel.getUserBalance()
            hiddenAccNum = formatAccountNumber(userData.accountNumber)
            username = userData.name
        }

        viewModel.userBalance.observe(viewLifecycleOwner) { balance ->
            userBalance = formatRupiah(balance.toString())
            hiddenBalance = userBalance.replace(Regex("\\d"), "*").replace(Regex("[,.]"), "")
        }


        val isFlashAvailable = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (isFlashAvailable) {
            binding.flashButton.visibility = View.VISIBLE
        } else {
            makeToast(requireContext(), "Flash tidak tersedia")
            binding.flashButton.visibility = View.GONE
        }


        binding.flashButton.setOnClickListener() {
            isOnFlash = !isOnFlash
            switchFlashLight(isOnFlash)
            codeScanner.isFlashEnabled = isOnFlash
        }




    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                codeScanner.startPreview()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    requireContext(),
                    "Izin kamera diperlukan untuk mengambil gambar",
                    Toast.LENGTH_SHORT
                ).show()
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

   private fun switchFlashLight(status: Boolean) {
//       if (status) {
//           cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
//       } else {
//           cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
//       }

       try {
           cameraId = cameraManager.cameraIdList[0]
           cameraManager.setTorchMode(cameraId, status)
       } catch (e: CameraAccessException) {
           e.printStackTrace()
       }
   }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncherGallery.launch(galleryIntent)
    }

    private var resultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                val imageUri = data!!.data!!
                val imagePath = convertMediaUriToPath(imageUri)
                val imgFile = File(imagePath)
                scanImageQRCode(imgFile)
            } else {
                Toast.makeText(requireContext(), "Result Not Found", Toast.LENGTH_LONG).show()
            }
        }

    private fun convertMediaUriToPath(uri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()

        return path
    }

    private fun scanImageQRCode(file: File) {
        val inputStream: InputStream = BufferedInputStream(FileInputStream(file))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val decoded = scanQRImage(bitmap)
        Toast.makeText(requireContext(), "$decoded", Toast.LENGTH_SHORT).show()
        sharedPreferences.edit().putString("accountDestinationFromScan", decoded).apply()

        val deepLinkToTrans = Uri.parse("app://com.example.app/trans/transferInputFromQR")
        view?.findNavController()?.navigate(deepLinkToTrans)
    }

    private fun scanQRImage(bMap: Bitmap): String? {
        var contents: String? = null
        val intArray = IntArray(bMap.width * bMap.height)
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bitmap)
            contents = result.text

        } catch (e: Exception) {
            Log.e("QrTest", "Error decoding qr code", e)
            Toast.makeText(
                requireContext(),
                "Error decoding QR Code, Mohon pilih gambar QR Code yang benar!",
                Toast.LENGTH_SHORT
            ).show()
        }
        return contents
    }

    private fun showDialogQRGeneratePage() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fragment_show_qris)
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations =
            com.synrgy7team4.common.R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        val tvShowQR = dialog.findViewById<TextView>(R.id.showQrTextView)
        val accountNo = dialog.findViewById<TextView>(R.id.accountNo)
        val qrImageView = dialog.findViewById<ImageView>(R.id.qrImageView)
        val ivToggleBalance = dialog.findViewById<ImageView>(R.id.iv_toggle_balance)
        val ammount = dialog.findViewById<TextView>(R.id.ammount)

        val getPayText = sharedPreferences.getString("payOrReceived", null)

        tvShowQR.text = "Menampilakn QRIS untuk $getPayText"
        accountNo.text = hiddenAccNum
        ammount.text = "Rp $hiddenBalance"

        val mQRBitmap = QRUtility.generateQR("$accountNumber $username")
        if (mQRBitmap != null) {
            qrImageView.setImageBitmap(mQRBitmap)
        } else {
            Toast.makeText(requireActivity(), "Failed to Generated QR Code", Toast.LENGTH_SHORT).show()
        }

        ivToggleBalance.setOnClickListener {
            if (isBalanceHidden) {
                ammount.text = "Rp $userBalance"
                ivToggleBalance.setImageResource(com.synrgy7team4.common.R.drawable.ic_visibility_on)
            } else {
                ammount.text = "Rp $hiddenBalance"
                ivToggleBalance.setImageResource(com.synrgy7team4.common.R.drawable.ic_visibility_off)
            }
            isBalanceHidden = !isBalanceHidden
        }
    }

    private fun formatAccountNumber(accNum: String): String {
        val visibleDigits = 3
        val length = accNum.length
        return if (length > visibleDigits) {
            val hiddenNum = "*".repeat(length - visibleDigits)
            val visibleNum = accNum.takeLast(visibleDigits)
            "$hiddenNum$visibleNum"
        } else {
            accNum
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}