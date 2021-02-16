package com.inces.incesclient.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inces.incesclient.R
import com.inces.incesclient.models.MyOrderProduct
import com.inces.incesclient.util.Constants
import kotlinx.android.synthetic.main.image_slider_layout.view.*
import kotlinx.android.synthetic.main.order_detail_card.view.*
import kotlinx.android.synthetic.main.product_card.view.*

class MyOrderDetailAdapter(
    private val keys: List<String>,
    private val productHash: HashMap<String, MyOrderProduct>
) :
    RecyclerView.Adapter<MyOrderDetailAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.order_detail_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            productName.text = productHash[keys[position]]!!.title

            if (!productHash[keys[position]]!!.base_image.contains("https"))
                Glide.with(context)
                        .asBitmap()
                        .load(Constants.IMAGE_URL + productHash[keys[position]]!!.base_image)
                        .into(iv_productImageOrders)
            else
                Glide.with(context)
                        .load(productHash[keys[position]]!!.base_image)
                        .into(iv_productImageOrders)

            val myOrderVariantDetailAdapter =
                MyOrderVariantDetailAdapter(productHash[keys[position]]!!.variants)
            variantDetailRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = myOrderVariantDetailAdapter
            }
        }
    }


    override fun getItemCount() = keys.size
}