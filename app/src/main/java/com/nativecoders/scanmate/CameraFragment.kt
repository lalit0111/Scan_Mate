package com.nativecoders.scanmate

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.nativecoders.scanmate.databinding.FragmentCameraBinding


class CameraFragment : Fragment(R.layout.fragment_camera) {

    lateinit var cameraKitView: CameraKitView
    lateinit var binding: FragmentCameraBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

        cameraKitView = binding.camera

        binding.capture.setOnClickListener {
            cameraKitView.captureImage { p0, image ->
                val bitmap = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                Log.d("capture", image.toString())
                binding.image.setImageBitmap(bitmap)
            }
        }

            binding.flash.setOnClickListener {
                if (cameraKitView.flash == CameraKit.FLASH_OFF) {
                    cameraKitView.flash = CameraKit.FLASH_ON
                } else {
                    cameraKitView.flash = CameraKit.FLASH_OFF
                }
                Log.d("flash", "on")
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