package com.nativecoders.scanmate

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ReorderAdapter(var list:ArrayList<Int> ) : RecyclerView.Adapter<ReorderAdapter.ViewHolder>() {
    class ViewHolder(var binding: ReorderListCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder= ViewHolder(
        ReorderListCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.apply {
           binding.listImageView.apply {
               setImageResource(list[position])

           }

       }
    }

    override fun getItemCount(): Int = list.size

    fun moveItem(from: Int, to: Int) {
        val fromEmoji = list[from]
        list.removeAt(from)
        if (to < from) {
            list.add(to, fromEmoji)
        } else {
            list.add(to - 1, fromEmoji)
        }
    }
}