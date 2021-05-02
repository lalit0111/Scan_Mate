package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
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
                .setFullscreenMode(false)
                .setDoneButtonString("Add")
                .setSinglePhotoMode(false)
                .setMax_photo(3)
                .setManualFocus(true)
                .setBucketName(javaClass.name)
                .setPreviewEnableCount(true)
                .setPreviewIconVisiblity(true)
                .setPreviewPageRedirection(true)
                .setEnableDone(false)
                .setClearBucket(true)
                .createCameraBundle()
        )
        startActivityForResult(intent, 214)

        var images = (activity as MainActivity).images

        binding.carouselView.apply {
            size = images.size
            resource = R.layout.list_card
            autoPlay = false
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                // Example here is setting up a full image carousel
                val imageView = view.findViewById<ImageView>(R.id.listImageView)
                imageView.setImageBitmap(images[position])
            }
            // After you finish setting up, show the CarouselView
            show()
        }
    }

    protected override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 214) {
            if (resultCode == RESULT_OK) {
                assert(data != null)
                list = data!!.getStringArrayExtra("resultData")
            }
        }
    }

}