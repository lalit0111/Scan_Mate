package com.nativecoders.scanmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nativecoders.scanmate.databinding.FragmentReorderBinding
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ReorderFragment : Fragment(R.layout.fragment_reorder) {

    lateinit var binding: FragmentReorderBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReorderBinding.bind(view)
        var images = ArrayList<Int>()
        images.add(R.drawable.img1)
        images.add(R.drawable.img2)
        images.add(R.drawable.img3)
        var adapter0= ReorderAdapter(images)
        binding.recView.apply {
            adapter = adapter0
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

}