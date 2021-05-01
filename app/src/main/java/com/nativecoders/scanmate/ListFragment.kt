package com.nativecoders.scanmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.nativecoders.scanmate.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var binding: FragmentListBinding
    var images = ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        images.add(R.drawable.img1)
        images.add(R.drawable.img2)
        images.add(R.drawable.img3)


        binding.carouselView.apply {
            size = images.size
            resource = R.layout.list_card
            autoPlay = false
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                // Example here is setting up a full image carousel
                val imageView = view.findViewById<ImageView>(R.id.listImageView)
                imageView.setImageDrawable(resources.getDrawable(images[position]))
            }
            // After you finish setting up, show the CarouselView
            show()
        }
    }

}