package com.inces.incesclient.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.internal.Mutable
import com.inces.incesclient.R
import com.inces.incesclient.models.Product
import kotlinx.android.synthetic.main.promoted_card.view.*

class FilterAdapter(
    private val supplierList: MutableList<String>,
    private val supplierProductList: MutableMap<String, MutableList<Product>>
) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.promoted_card, parent, false)
        )
    }

    override fun getItemCount() = supplierProductList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            supplierName.text = supplierList[position]
            val promotedProductsAdapter = PromotedProductsAdapter()
            promotedRecycler.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = promotedProductsAdapter
            }
            promotedProductsAdapter.differ.submitList(supplierProductList[supplierList[position]])
        }
    }
}