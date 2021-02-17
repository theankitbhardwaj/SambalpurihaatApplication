package com.inces.incesclient.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.activities.Level3CategoryActivity
import com.inces.incesclient.R
import kotlinx.android.synthetic.main.category_card.view.*

class Level3CategoryAdapter (
        private val categoryList: MutableList<String>,
        private val subCategory: String
    ) : RecyclerView.Adapter<Level3CategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
        )
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            setOnClickListener {
                val i = Intent(context, Level3CategoryActivity::class.java)
                i.putExtra("category", categoryList[position])
                i.putExtra("subCategory", subCategory)
                context.startActivity(i)
            }
            categoryName.text = categoryList[position]
        }
    }
}