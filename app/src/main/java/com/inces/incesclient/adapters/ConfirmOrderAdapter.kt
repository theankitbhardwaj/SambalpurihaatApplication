package com.inces.incesclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.db.CartRoom
import com.inces.incesclient.helpers.ProductHelper
import kotlinx.android.synthetic.main.confirm_order_product_layout.view.*

class ConfirmOrderAdapter : RecyclerView.Adapter<ConfirmOrderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<CartRoom>() {
        override fun areItemsTheSame(
            oldItem: CartRoom,
            newItem: CartRoom
        ): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(
            oldItem: CartRoom,
            newItem: CartRoom
        ): Boolean {
            return oldItem.productId == newItem.productId
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.confirm_order_product_layout, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = differ.currentList[position]
        holder.itemView.apply {
            product.text =
                "${cart.title} - ${cart.variants.size} Variant(s)"
            var totalPrice = 0
            cart.variants.forEach {
                totalPrice +=
                    it.finalPrice

            }
            price.text = "- \u20B9 $totalPrice"
        }
    }

    override fun getItemCount() = differ.currentList.size
}