package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import `in`.balakrishnan.easycam.FileUtils
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nativecoders.scanmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding
    lateinit var bitmapList:ArrayList<Bitmap>
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    lateinit var images: ArrayList<Bitmap>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        images = ArrayList()
        bitmapList = ArrayList()
        navController = findNavController(R.id.nav_host_fragment)

        startCamera()

        val bottomSheet = findViewById<ConstraintLayout>(R.id.filterBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
    }

    private fun startCamera() {
        val intent = Intent(this, CameraControllerActivity::class.java)
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
                        bitmapList.add(BitmapFactory.decodeFile(item))
                    }
                }
                navController.navigate(R.id.action_listFragment_self)
            }
        }
    }

    override fun onDestroy() {
        FileUtils.clearAllFiles(this, localClassName);
        super.onDestroy()
    }
}