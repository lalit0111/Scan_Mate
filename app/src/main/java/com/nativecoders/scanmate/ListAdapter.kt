package com.nativecoders.scanmate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecoders.scanmate.databinding.ListCardBinding
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ListAdapter(var activity: MainActivity) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder= ViewHolder(
        ListCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.apply {
           binding.listImageView.apply {
               setImageBitmap(activity.bitmapList[position])
           }
           val page = "${position + 1}/${activity.bitmapList.size}"
           binding.pageCount.text = page
           binding.deleteImage.setOnClickListener {
               activity.bitmapList.removeAt(position)
               notifyDataSetChanged()
           }

       }
    }

    override fun getItemCount(): Int = activity.bitmapList.size

}