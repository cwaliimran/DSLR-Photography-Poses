package com.cwnextgen.hdwallpapers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.hdwallpapers.databinding.RowHomeBinding
import com.cwnextgen.hdwallpapers.models.WallpaperModel
import com.cwnextgen.hdwallpapers.utils.OnItemClick

class HomeAdapter(
    private var mList: MutableList<WallpaperModel>,
    private var listener: OnItemClick
) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding = RowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = mList[position]

        holder.binding.data = model
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(modelPostsWithUsers: MutableList<WallpaperModel>) {
        this.mList = modelPostsWithUsers
        notifyDataSetChanged()

    }

    inner class MyViewHolder(
        val binding: RowHomeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}