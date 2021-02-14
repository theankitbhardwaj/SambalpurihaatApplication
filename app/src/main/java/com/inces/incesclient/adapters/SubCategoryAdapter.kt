package com.inces.incesclient.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.activities.CategoryActivity
import com.inces.incesclient.activities.SubCategoryActivity
import com.inces.incesclient.models.Product
import kotlinx.android.synthetic.main.category_card.view.*

class SubCategoryAdapter(
    private val subCategoryList: MutableList<String>,
    private val mainCategory: String
) :
    RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
        )
    }

    override fun getItemCount() = subCategoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            setOnClickListener {
                val i = Intent(context, SubCategoryActivity::class.java)
                i.putExtra("subCategory", subCategoryList[position])
                i.putExtra("mainCategory", mainCategory)
                context.startActivity(i)
            }
            categoryName.text = subCategoryList[position]
        }
    }

}