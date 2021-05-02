package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import android.app.Activity.RESULT_OK
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.labters.documentscanner.helpers.ScannerConstants
import com.nativecoders.scanmate.databinding.FragmentListBinding
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.model.AspectRatio
import java.io.ByteArrayOutputStream
import java.io.File

class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var binding: FragmentListBinding
    var imagePosition = 0
    lateinit var bitmaps: ArrayList<Bitmap>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        bitmaps = ArrayList()

        val intent = Intent(requireContext(), CameraControllerActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(
            "inputData", CameraBundleBuilder()
                .setFullscreenMode(true)
                .setSinglePhotoMode(false)
                .setEnableDone(false)
                .setCaptureButtonDrawable(R.drawable.group_1)
                .setMax_photo(50)
                .setManualFocus(false)
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
                if (images != null) {
                    for (item in images) {
                        bitmaps.add(BitmapFactory.decodeFile(item))
                    }
                }

                binding.carouselView.apply {
                    size = images?.size!!
                    resource = R.layout.list_card
                    autoPlay = false
                    indicatorAnimationType = IndicatorAnimationType.THIN_WORM
                    carouselOffset = OffsetType.CENTER
                    setCarouselViewListener { view, position ->
                        val imageView = view.findViewById<ImageView>(R.id.listImageView)
                        imageView.setImageBitmap(bitmaps[position])
                        imagePosition = position
                    }
                    show()
                }
                binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.reorderImage -> {
                            findNavController().navigate(R.id.action_listFragment_to_reorderFragment)
                            true
                        }
                        R.id.rotateImage -> {
                            bitmaps[imagePosition] = bitmaps[imagePosition].rotate(90f)
                            binding.carouselView.setCarouselViewListener { view, position ->
                                val imageView = view.findViewById<ImageView>(R.id.listImageView)
                                imageView.setImageBitmap(bitmaps[position])
                                imagePosition = position
                            }
                            true
                        }
                        R.id.cropImage -> {
                            cropImage()
                            true
                        }
                        else -> false
                    }
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resultUri)

        }
    }

    private fun cropImage() {
        val option = UCrop.Options()
        var uri = getImageUri(requireContext(),bitmaps[imagePosition])
        option.setAspectRatioOptions(
            2,
            AspectRatio("1:2", 1f, 2f),
            AspectRatio("3:4", 3f, 4f),
            AspectRatio("Default", 9f, 16f),
            AspectRatio("4:3", 4f, 3f),
            AspectRatio("2:1", 2f, 1f)
        )
        if (uri != null) {
            UCrop.of(uri,Uri.fromFile(File(requireContext().cacheDir, "Vision")))
                .withOptions(option)
                .start(activity as MainActivity)
        } else {
            Toast.makeText(
                requireContext(),
                "Wait for loading to complete then try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

        fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
            var bytes = ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            var path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Title",
                null
            );
            return Uri.parse(path);
        }

        fun Bitmap.rotate(degrees: Float): Bitmap {
            val matrix = Matrix().apply { postRotate(degrees) }
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }
    }