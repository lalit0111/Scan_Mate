package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.navigation.fragment.findNavController
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.nativecoders.scanmate.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var binding: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        val intent = Intent(requireContext(), CameraControllerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(
            "inputData", CameraBundleBuilder()
                .setFullscreenMode(true)
                .setSinglePhotoMode(false)
                .setEnableDone(false)
                .setCaptureButtonDrawable(R.drawable.group_1)
                .setMax_photo(50)
                .setManualFocus(true)
                .setBucketName(javaClass.name)
                .setPreviewEnableCount(true)
                .setPreviewIconVisiblity(true)
                .setPreviewPageRedirection(true)
                .setClearBucket(true)
                .createCameraBundle()
        )
        startActivityForResult(intent, 214)

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 214) {
            if (resultCode == RESULT_OK) {
                assert(data != null)
                var images = data!!.getStringArrayExtra("resultData")

                binding.carouselView.apply {
                    size = images?.size!!
                    resource = R.layout.list_card
                    autoPlay = false
                    indicatorAnimationType = IndicatorAnimationType.THIN_WORM
                    carouselOffset = OffsetType.CENTER
                    setCarouselViewListener { view, position ->
                        val imageView = view.findViewById<ImageView>(R.id.listImageView)
                        val bitmap = BitmapFactory.decodeFile(images[position])
                        imageView.setImageBitmap(bitmap)
                    }
                    show()
                }
                binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.reorderImage -> {
                            findNavController().navigate(R.id.action_listFragment_to_reorderFragment)
                            true
                        }
                        else -> false
                    }
                }
            }

        }
    }
}