package com.nativecoders.scanmate

import `in`.balakrishnan.easycam.CameraBundleBuilder
import `in`.balakrishnan.easycam.CameraControllerActivity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textview.MaterialTextView
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import com.nativecoders.scanmate.databinding.FragmentListBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.yalantis.ucrop.UCrop
import net.alhazmy13.imagefilter.ImageFilter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


class ListFragment : Fragment(R.layout.fragment_list) {
    lateinit var binding: FragmentListBinding
    private val PICK_IMAGE = 10
    lateinit var vAdapter: ListAdapter
    lateinit var originalBitmaps : ArrayList<Bitmap>
    var imagePosition = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        setUpViewPager()
        setupBottomSheet()
        originalBitmaps = ArrayList((activity as MainActivity).bitmapList)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.reorderImage -> {
                    findNavController().navigate(R.id.action_listFragment_to_reorderFragment)
                    true
                }
                R.id.rotateImage -> {
                    (activity as MainActivity).bitmapList[imagePosition] =
                        (activity as MainActivity).bitmapList[imagePosition].rotate(90f)
                    vAdapter.notifyDataSetChanged()
                    true
                }
                R.id.cropImage -> {
                    cropImage()
                    true
                }
                R.id.addImage -> {
                    chooseImgFromGallery()
                    true
                }
                R.id.colorImage ->{
                    (activity as MainActivity).bottomSheetBehavior.isDraggable = false
                    (activity as MainActivity).bottomSheetBehavior.state =
                        BottomSheetBehavior.STATE_EXPANDED
                    true
                }
                else -> false
            }
        }
    }

    fun colorImage(imageFilter: ImageFilter.Filter?){
        var  bitmap = originalBitmaps[imagePosition]
        if(imageFilter != null){
            bitmap = ImageFilter.applyFilter(originalBitmaps[imagePosition], imageFilter)
        }
        (activity as MainActivity).bitmapList[imagePosition] = bitmap
        vAdapter.notifyDataSetChanged()
    }

    fun chooseImgFromGallery() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
    }

    private fun setUpViewPager() {
        vAdapter = ListAdapter(activity as MainActivity)
        binding.viewPager.apply {
            adapter = vAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    imagePosition = position
                    super.onPageSelected(position)
                }
            })
        }
    }


        override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            @Nullable data: Intent?
        ) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE) {
                val imageUri: Uri? = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    imageUri
                )
                (activity as MainActivity).bitmapList.add(0, bitmap)
                vAdapter.notifyDataSetChanged()
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    val resultUri = result.uri
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireContext().contentResolver, resultUri)
                    Log.d("bitmap", bitmap.toString())
                    (activity as MainActivity).bitmapList[imagePosition] = bitmap
                    vAdapter.notifyDataSetChanged()
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }
    }

    private fun cropImage() {
        var uri = getImageUri(
            requireContext(),
            (activity as MainActivity).bitmapList[imagePosition]
        )
        CropImage.activity(uri)
            .start(requireContext(), this);
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

    fun ImageToPdf(){
        var document = Document()
        val directoryPath = Environment.getExternalStorageDirectory().toString()
        PdfWriter.getInstance(
            document,
            FileOutputStream("$directoryPath/mohit11.pdf")
        )
        document.open()

        for(b in (activity as MainActivity).bitmapList){
            val image:Image = Image.getInstance(b.toByteArray())
            val scaler = (document.pageSize.width - document.leftMargin()
                    - document.rightMargin() - 0) / image.width * 100 // 0 means you have no indentation. If you have any, change it.

            image.scalePercent(scaler)
            image.alignment = Image.ALIGN_CENTER or Image.ALIGN_TOP

            document.add(image)
        }
        document.close()
        Toast.makeText(requireContext(),"Saved",Toast.LENGTH_SHORT).show()

    }

    private fun Bitmap.toByteArray():ByteArray{
        val size: Int = this.rowBytes * this.height
        val byteBuffer = ByteBuffer.allocate(size)
        this.copyPixelsToBuffer(byteBuffer)
        return byteBuffer.array()
    }

    private fun setupBottomSheet() {

        val cancel = (activity as MainActivity).findViewById<MaterialTextView>(R.id.sortTv)
        val none = (activity as MainActivity).findViewById<MaterialTextView>(R.id.none)
        val greyscale = (activity as MainActivity).findViewById<MaterialTextView>(R.id.greyscale)
        val relief =
            (activity as MainActivity).findViewById<MaterialTextView>(R.id.relief)
        val average = (activity as MainActivity).findViewById<MaterialTextView>(R.id.average_blur)
        val neon =
            (activity as MainActivity).findViewById<MaterialTextView>(R.id.neon)
        val oil = (activity as MainActivity).findViewById<MaterialTextView>(R.id.oil)

        var checked = none
        greyscale.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        relief.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        average.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        neon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        oil.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        none.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)

        none.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            none.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            checked = none
            colorImage(null)
        }

        greyscale.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            greyscale.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            checked = greyscale
            colorImage(ImageFilter.Filter.GRAY)
        }

        relief.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            relief.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            checked = relief
            colorImage(ImageFilter.Filter.RELIEF)
        }

        average.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            average.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            colorImage(ImageFilter.Filter.AVERAGE_BLUR)
        }

        neon.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            neon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            checked = neon
            colorImage(ImageFilter.Filter.NEON)
        }

        oil.setOnClickListener {
            checked.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            oil.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_24, 0)
            checked = oil
            colorImage(ImageFilter.Filter.OIL)
        }

        cancel.setOnClickListener {
            (activity as MainActivity).bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

}
