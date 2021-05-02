package com.nativecoders.scanmate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecoders.scanmate.databinding.ListCardBinding
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ListAdapter(var list:ArrayList<Bitmap> ) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder= ViewHolder(
        ListCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.apply {
           binding.listImageView.apply {
               setImageBitmap(list[position])
           }
           val page = "${position + 1}/${list.size}"
           binding.pageCount.text = page

       }
    }

    override fun getItemCount(): Int = list.size

}