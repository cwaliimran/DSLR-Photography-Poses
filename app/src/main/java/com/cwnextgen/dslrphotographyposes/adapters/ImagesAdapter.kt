package com.cwnextgen.dslrphotographyposes.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
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

        Glide.with(context)
            .load(model.imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image loading failed so hide progress bar
                    holder.binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image was loaded successfully, hide the progress bar
                    holder.binding.progressBar.visibility = View.GONE
                    return false
                }


            })
            .into(holder.binding.imageView)
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