package com.inces.incesclient.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.inces.incesclient.R
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.models.Variant
import kotlinx.android.synthetic.main.cart_variant_card.view.*
import kotlinx.android.synthetic.main.variant_bottom_card.view.*
import kotlinx.android.synthetic.main.variant_bottom_card.view.oldPrice
import kotlinx.android.synthetic.main.variant_card.view.variantPrice
import kotlinx.android.synthetic.main.variant_card.view.variantQuantity
import kotlinx.android.synthetic.main.variant_card.view.variantTitle

class VariantAdapter(private val breakDownListener: BreakDownListener) :
    RecyclerView.Adapter<VariantAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val updatedList: MutableList<Variant> = ArrayList()

    private val differCallBack = object : DiffUtil.ItemCallback<Variant>() {
        override fun areItemsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem.variant_id == newItem.variant_id
        }

        override fun areContentsTheSame(oldItem: Variant, newItem: Variant): Boolean {
            return oldItem.variant_id == newItem.variant_id
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.variant_bottom_card, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        updatedList.clear()
        updatedList.addAll(differ.currentList)
        val variant = differ.currentList[position]
        holder.itemView.apply {

            if (variant.discount != 0)
                discount.text = "${variant.discount}% off"
            var quantity = 0
            val totalPrice = variant.price
            val availableStock = variant.quantity
            if (availableStock == 0) {
                outOfStock.visibility = View.VISIBLE
                quantityLayout.visibility = View.GONE
            } else {
                outOfStock.visibility = View.GONE
                quantityLayout.visibility = View.VISIBLE
            }
            updatedList[position].selectedQuantity = 0
            updatedList[position].finalPrice = ProductHelper.getDiscountPriceToInt(variant.discount, (totalPrice * quantity))!!

            increase.setOnClickListener {
                if (quantity < availableStock)
                    quantity++
                else
                    Toast.makeText(
                        context,
                        "You cannot add more than available stock",
                        Toast.LENGTH_SHORT
                    ).show()
                variantQuantity.text = quantity.toString()

                updatedList[position].selectedQuantity = quantity
                updatedList[position].finalPrice =
                    ProductHelper.getDiscountPriceToInt(variant.discount, (totalPrice * quantity))!!
                breakDownListener.updatePriceAndQuantity()
            }

            decrease.setOnClickListener {
                if (quantity != 0)
                    quantity--

                variantQuantity.text = quantity.toString()

                updatedList[position].selectedQuantity = quantity
                updatedList[position].finalPrice =
                    ProductHelper.getDiscountPriceToInt(variant.discount, (totalPrice * quantity))!!
                breakDownListener.updatePriceAndQuantity()
            }

            variantTitle.text = variant.variant_name
            variantQuantity.text = quantity.toString()
            if (variant.discount != 0) {
                oldPrice.visibility = View.VISIBLE
                oldPrice.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = "\u20B9 ${variant.price}"
                }
                variantPrice.text =
                    "\u20B9 ${ProductHelper.getDiscountPrice(variant.discount, totalPrice)}"
            } else {
                oldPrice.visibility = View.GONE
                variantPrice.text = "\u20B9 ${variant.price}"
            }


            variantImages.setSliderAdapter(ProductDetailImageAdapter(variant.image.sortedBy { it.pos }))
        }
    }

    fun getUpdatedList() = updatedList

    interface BreakDownListener {
        fun updatePriceAndQuantity()
    }

    override fun getItemCount() = differ.currentList.size
}