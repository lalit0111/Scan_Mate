package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.nativecoders.scanmate.databinding.FragmentListBinding


class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var binding: FragmentListBinding
    private val PICK_IMAGE = 10
    lateinit var  vAdapter:ListAdapter
    var currentImg:Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        startCamera()


    }

    fun chooseImgFromGallery(){
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun startCamera(){
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

    private fun setUpViewPager(list: ArrayList<String>?) {
        vAdapter = ListAdapter(list!!)
        binding.viewPager.apply {
            adapter = vAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL

        }
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
                var list = images?.toCollection(ArrayList())
                setUpViewPager(list)
                binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.reorderImage -> {
                            findNavController().navigate(R.id.action_listFragment_to_reorderFragment)
                            true
                        }
                        else -> false
                    }
                }
            }else if (requestCode == PICK_IMAGE) {
                val imageUri: Uri? = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
              }

        }
    }


}