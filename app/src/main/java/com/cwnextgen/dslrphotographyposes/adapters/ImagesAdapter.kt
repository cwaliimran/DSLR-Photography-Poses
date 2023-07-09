package com.cwnextgen.dslrphotographyposes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.dslrphotographyposes.databinding.RowImagesBinding
import com.network.models.ImagesModel
import com.cwnextgen.dslrphotographyposes.utils.OnItemClick

class ImagesAdapter(
    private var mList: MutableList<ImagesModel>,
    private var listener: OnItemClick
) : RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding = RowImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = mList[position]

        holder.binding.data = model
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(modelPostsWithUsers: MutableList<ImagesModel>) {
        this.mList = modelPostsWithUsers
        notifyDataSetChanged()

    }

    inner class MyViewHolder(
        val binding: RowImagesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}