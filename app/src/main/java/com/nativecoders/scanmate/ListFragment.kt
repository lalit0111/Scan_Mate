package com.nativecoders.scanmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import com.nativecoders.scanmate.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var binding: FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        var images = (activity as MainActivity).images

        binding.carouselView.apply {
            size = images.size
            resource = R.layout.list_card
            autoPlay = false
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                val imageView = view.findViewById<ImageView>(R.id.listImageView)
                imageView.setImageBitmap(images[position])
            }
            show()
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.reorderImage -> {
                    findNavController().navigate(R.id.action_listFragment_to_reorderFragment)
                    true
                }
                else -> false
            }
        }
    }


}