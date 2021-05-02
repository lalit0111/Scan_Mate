package com.nativecoders.scanmate

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ReorderAdapter(var activity: MainActivity ) : RecyclerView.Adapter<ReorderAdapter.ViewHolder>() {
    class ViewHolder(var binding: ReorderListCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder= ViewHolder(
        ReorderListCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.apply {
           binding.listImageView.apply {
               setImageBitmap(activity.bitmapList[position])
           }

       }
    }

    override fun getItemCount(): Int = activity.bitmapList.size

    fun moveItem(from: Int, to: Int) {
        val fromEmoji = activity.bitmapList[from]
        activity.bitmapList.removeAt(from)
        if (to < from) {
            activity.bitmapList.add(to, fromEmoji)
        } else {
            activity.bitmapList.add(to - 1, fromEmoji)
        }

        Log.d("images" , activity.bitmapList.toString())
    }
}