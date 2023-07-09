package com.cwnextgen.dslrphotographyposes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cwnextgen.dslrphotographyposes.databinding.RowCategoriesBinding
import com.network.models.CategoriesModel
import com.cwnextgen.dslrphotographyposes.utils.OnItemClick

class CategoriesAdapter(
    private var mList: MutableList<CategoriesModel>,
    private var listener: OnItemClick
) : RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding =
            RowCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = mList[position]

        holder.binding.data = model


   /*     //dynamic background

// Define a list of dynamic colors.
        val standardColors = listOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN,
            Color.MAGENTA, Color.BLACK, Color.DKGRAY, Color.LTGRAY, Color.GRAY,
            Color.WHITE)

        val randomColors = List(30) { Color.rgb((0..255).random(), (0..255).random(), (0..255).random()) }

        val dynamicColors = standardColors + randomColors

// Select a random color from the list.
        val dynamicColor = dynamicColors.random()

// Extract RGB values.
        val red = Color.red(dynamicColor)
        val green = Color.green(dynamicColor)
        val blue = Color.blue(dynamicColor)

// Set alpha for transparency. 128 is approximately 50% transparent.
        val color = Color.argb(70, red, green, blue)
        val colorTv = Color.argb(255, red, green, blue)
        holder.binding.title.setTextColor(colorTv)
        holder.binding.title.setBackgroundColor(color)
        holder.binding.title.setShadowLayer(1.6f,1f,1f,Color.WHITE);
*/

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(modelPostsWithUsers: MutableList<CategoriesModel>) {
        this.mList = modelPostsWithUsers
        notifyDataSetChanged()

    }

    inner class MyViewHolder(
        val binding: RowCategoriesBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }
}