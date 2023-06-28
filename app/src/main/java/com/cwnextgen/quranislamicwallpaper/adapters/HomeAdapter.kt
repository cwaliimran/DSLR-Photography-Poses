package com.cwnextgen.quranislamicwallpaper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.quranislamicwallpaper.databinding.RowHomeBinding
import com.cwnextgen.quranislamicwallpaper.models.MainModel
import com.cwnextgen.quranislamicwallpaper.utils.OnItemClick

class HomeAdapter(
    private var mList: MutableList<MainModel>,
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

    fun updateData(modelPostsWithUsers: MutableList<MainModel>) {
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