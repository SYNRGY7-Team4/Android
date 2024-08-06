package com.synrgy7team4.feature_auth.presentation.register

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentFotoKtpBinding

class FotoKtpFragment : Fragment()/*, ImageCapture.OnImageSavedCallback */{
    private var _binding: FragmentFotoKtpBinding? = null
    private val binding get() = _binding!!
    /*private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider

    private val backCameraSelector by lazy {
        getCameraSelector(CameraSelector.LENS_FACING_BACK)
    }

    private val frontCameraSelector by lazy {
        getCameraSelector(CameraSelector.LENS_FACING_FRONT)
    }

    private val cameraPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission not available closing app",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFotoKtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCapture.setOnClickListener {
           // requireView().findNavController().navigate(R.id.action_fotoKtpFragment_to_verifikasiKtpFragment)
        }
        /*// Check for camera permission and request if not granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionResult.launch(Manifest.permission.CAMERA)
        } else {
            startCamera()
        }*/
    }

    /*private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, backCameraSelector)
        }, ContextCompat.getMainExecutor(requireContext()))

        binding.btnCapture.setOnClickListener {
            takePicture()
        }

        binding.btnFlipCamera.setOnClickListener {
            if (!this::camera.isInitialized && !this::cameraProvider.isInitialized) return@setOnClickListener

            if (camera.cameraInfo.lensFacing == CameraSelector.LENS_FACING_FRONT) {
                cameraProvider.unbindAll()
                bindPreview(cameraProvider, backCameraSelector)
            } else {
                cameraProvider.unbindAll()
                bindPreview(cameraProvider, frontCameraSelector)
            }
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider, cameraSelector: CameraSelector) {
        val preview = Preview.Builder().build()
        val previewView = binding.previewView

        preview.setSurfaceProvider(previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()

        camera = cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    private fun takePicture() {
        requireView().findNavController().navigate(R.id.action_fotoKtpFragment_to_verifikasiKtpFragment)
        if (!this::imageCapture.isInitialized) return

        imageCapture.takePicture(
            getImageOutputOptions(),
            ContextCompat.getMainExecutor(requireContext()),
            this
        )
    }

    private fun getCameraSelector(lensFacing: Int) =
        CameraSelector.Builder().requireLensFacing(lensFacing).build()

    private fun getImageOutputOptions(): ImageCapture.OutputFileOptions {
        val contentResolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        return ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
    }

    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        Snackbar.make(
            binding.root,
            "Image saved successfully",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onError(exception: ImageCaptureException) {
        exception.printStackTrace()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
