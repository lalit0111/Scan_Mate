package com.nativecoders.scanmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nativecoders.scanmate.databinding.FragmentReorderBinding
import com.nativecoders.scanmate.databinding.ReorderListCardBinding

class ReorderFragment : Fragment(R.layout.fragment_reorder) {

    lateinit var binding: FragmentReorderBinding
    lateinit var reorderAdapter: ReorderAdapter
    lateinit var itemTouchHelper: ItemTouchHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReorderBinding.bind(view)
        var images = ArrayList<Int>()
        images.add(R.drawable.img1)
        images.add(R.drawable.img2)
        images.add(R.drawable.img3)
       reorderAdapter = ReorderAdapter(images)
        binding.recView.apply {
            adapter = reorderAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(UP or
                DOWN or
                START or
                END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {

                val adapter = recyclerView.adapter as ReorderAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                // 2. Update the backing model. Custom implementation in
                //    MainRecyclerViewAdapter. You need to implement
                //    reordering of the backing model inside the method.
                adapter.moveItem(from, to)
                // 3. Tell adapter to render the model update.
                adapter.notifyItemMoved(from, to)

                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                  direction: Int) { }
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)

                viewHolder.itemView.alpha = 1.0f
            }
        }

    fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

}
