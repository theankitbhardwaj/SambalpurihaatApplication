package com.inces.incesclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.db.CartRoom
import kotlinx.android.synthetic.main.cart_card.view.*

class CartAdapter(private val listener: ClickListener) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
            R.layout.cart_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            productTitle.text = differ.currentList[position].title
            productVariant.text =
                "${differ.currentList[position].variants.size} Variants"
            val images: MutableList<String> = ArrayList()
            differ.currentList[position].variants[0].image.forEach {
                images.add(it.img)
            }
            productImage.setSliderAdapter(ImageSliderAdapter(images))
            val cartVariantAdapter = CartVariantAdapter()
            cartVariantRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = cartVariantAdapter
            }
            cartVariantAdapter.differ.submitList(differ.currentList[position].variants)

            remove.setOnClickListener {
                listener.deleteProduct(differ.currentList[position])
            }
        }
    }

    interface ClickListener {
        fun deleteProduct(cartRoom: CartRoom)
    }

    override fun getItemCount() = differ.currentList.size
}