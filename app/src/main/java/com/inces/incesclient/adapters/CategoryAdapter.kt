package com.inces.incesclient.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inces.incesclient.R
import com.inces.incesclient.activities.FilterActivity
import com.inces.incesclient.models.CategoryItem
import com.inces.incesclient.util.Constants
import kotlinx.android.synthetic.main.category_card.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(
            oldItem: CategoryItem,
            newItem: CategoryItem
        ): Boolean {
            return oldItem.category_name == newItem.category_name
        }

        override fun areContentsTheSame(
            oldItem: CategoryItem,
            newItem: CategoryItem
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {
            Log.e("TAG", "onBindViewHolder: ${category.category_name}")
            setOnClickListener {
                val i = Intent(context, FilterActivity::class.java)
                i.putExtra("category", category.category_name)
                context.startActivity(i)
            }
            Glide.with(context)
                .load(Constants.CATEGORY_ICON_URL + category.icon)
                .into(categoryImage)
            categoryName.text = category.category_name
        }
    }

}