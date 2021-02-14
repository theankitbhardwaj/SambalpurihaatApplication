package com.inces.incesclient.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.models.Variant
import kotlinx.android.synthetic.main.cart_variant_card.view.*
import kotlinx.android.synthetic.main.cart_variant_card.view.variantPrice
import kotlinx.android.synthetic.main.cart_variant_card.view.variantQuantity
import kotlinx.android.synthetic.main.cart_variant_card.view.variantTitle

class CartVariantAdapter : RecyclerView.Adapter<CartVariantAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallBack = object : DiffUtil.ItemCallback<Variant>() {
        override fun areItemsTheSame(
            oldItem: Variant,
            newItem: Variant
        ): Boolean {
            return oldItem.variant_id == newItem.variant_id
        }

        override fun areContentsTheSame(
            oldItem: Variant,
            newItem: Variant
        ): Boolean {
            return oldItem.variant_id == newItem.variant_id
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.cart_variant_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val variant = differ.currentList[position]
        holder.itemView.apply {
            variantTitle.text = variant.variant_name
            variantQuantity.text = variant.selectedQuantity.toString()
            if (variant.discount == 0) {
                oldPrice.visibility = View.GONE
                variantPrice.text = "\u20B9 ${variant.finalPrice}"
            } else {
                oldPrice.visibility = View.VISIBLE
                oldPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = "\u20B9 ${variant.price * variant.selectedQuantity}"
                }
                variantPrice.text =
                    "\u20B9 ${variant.finalPrice}"
            }
        }

    }

    override fun getItemCount() = differ.currentList.size
}