package com.inces.incesclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.models.Variant
import kotlinx.android.synthetic.main.variant_detail_card.view.*

class MyOrderVariantDetailAdapter(private val variantList: List<Variant>) :
    RecyclerView.Adapter<MyOrderVariantDetailAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.variant_detail_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            variantName.text = "${
                variantList[position].variant_name
            } - [${variantList[position].quantity}]"
        }
    }


    override fun getItemCount() = variantList.size
}