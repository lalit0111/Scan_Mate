package com.nativecoders.scanmate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.nativecoders.scanmate.databinding.FragmentCameraBinding


class CameraFragment : Fragment(R.layout.fragment_camera) {

    lateinit var cameraKitView: CameraKitView
    lateinit var binding: FragmentCameraBinding
    lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

        cameraKitView = binding.camera
        mainActivity = activity as MainActivity

        binding.capture.setOnClickListener {
            cameraKitView.captureImage(object : CameraKitView.ImageCallback {
                override fun onImage(p0: CameraKitView?, image: ByteArray?) {
                    val bitmap = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                    Log.d("capture", image.toString())
                    binding.image.setImageBitmap(bitmap)
                    mainActivity.images.add(bitmap)
                    binding.imageCount.text = mainActivity.images.size.toString()
                }
            })
        }

        binding.rotate.setOnClickListener {
            cameraKitView.toggleFacing()
        }

        binding.image.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_listFragment)
        }

            binding.flash.setOnClickListener {
                cameraKitView.flash = CameraKit.FLASH_ON
                Log.d("flash", cameraKitView.hasFlash().toString())
            }
        }

        override fun onStart() {
            super.onStart()
            cameraKitView.onStart()
        }

        override fun onResume() {
            super.onResume()
            cameraKitView.onResume()
        }

        override fun onPause() {
            cameraKitView.onPause()
            super.onPause()
        }

        override fun onStop() {
            cameraKitView.onStop()
            super.onStop()
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }