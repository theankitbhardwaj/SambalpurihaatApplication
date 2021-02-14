package com.inces.incesclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.inces.incesclient.R
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.MyOrderProduct
import com.inces.incesclient.models.Order
import kotlinx.android.synthetic.main.my_order_card.view.*

class MyOrderAdapter(private val listener: ClickListener) :
    RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return oldItem.orderID == newItem.orderID
        }

        override fun areContentsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.my_order_card, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.itemView.apply {
            val hashKeys = order.productList.keySet()
            val productHash: HashMap<String, MyOrderProduct> = HashMap()
            val keys: MutableList<String> = ArrayList()
            var totalPrice = 0
            var totalQuantity = 0

            hashKeys.forEach {
                keys.add(it)
                productHash[it] = Gson().fromJson(
                    order.productList.get(it),
                    MyOrderProduct::class.java
                )


                productHash[it]!!.variants.forEach {
                    totalPrice += it.price * it.quantity
                    totalQuantity += it.quantity
                }
            }

            if (order.orderStatus == "Active") {
                cancel.visibility = View.VISIBLE
                orderStatus.setTextColor(context.resources.getColor(R.color.orderActive))
            } else {
                cancel.visibility = View.GONE
                orderStatus.setTextColor(context.resources.getColor(R.color.orderCancelled))
            }

            orderNumber.text = "Order No : ${order.orderID}"
            orderStatus.text = order.orderStatus
            orderDate.text = UserHelper.toDate(order.orderDate)
            orderPrice.text = "\u20B9 $totalPrice"
            orderQuantity.text = totalQuantity.toString()

            val myOrderDetailAdapter = MyOrderDetailAdapter(keys, productHash)
            orderProductRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = myOrderDetailAdapter
            }

            cancel.setOnClickListener {
                listener.cancelOrder(order.orderID)
            }

        }
    }

    interface ClickListener {
        fun cancelOrder(orderId: Int)
    }


    override fun getItemCount() = differ.currentList.size
}