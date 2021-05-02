package com.nativecoders.scanmate

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.nativecoders.scanmate.databinding.FragmentCameraBinding
import com.priyankvasa.android.cameraviewex.CameraView
import com.priyankvasa.android.cameraviewex.Modes
import java.nio.ByteBuffer


class CameraFragment : Fragment(R.layout.fragment_camera) {

    lateinit var cameraKitView: CameraKitView
    lateinit var binding: FragmentCameraBinding
    lateinit var mainActivity: MainActivity
    lateinit var camera: CameraView

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

        camera = binding.camera
        mainActivity = activity as MainActivity

        camera.addCameraOpenedListener {
            binding.capture.setOnClickListener {
                camera.addPictureTakenListener {
                    val myBitmap = BitmapFactory.decodeByteArray(it.data, 0, it.data.size, null)
                    binding.image.setImageBitmap(myBitmap)
                    mainActivity.images.add(myBitmap)
                    binding.imageCount.text = mainActivity.images.size.toString()
                }
                camera.capture()
            }
        }

        binding.image.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_listFragment)
        }

        binding.flash.setOnClickListener {
            camera.flash = Modes.Flash.FLASH_ON
        }

        binding.rotate.setOnClickListener {
            run {
                camera.facing = when (camera.facing) {
                    Modes.Facing.FACING_BACK -> Modes.Facing.FACING_FRONT
                    else -> Modes.Facing.FACING_BACK
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        camera.destroy()
        super.onDestroyView()
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