package com.inces.incesclient.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inces.incesclient.R
import com.inces.incesclient.adapters.CartAdapter
import com.inces.incesclient.adapters.ConfirmOrderAdapter
import com.inces.incesclient.db.CartRoom
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.helpers.SharedPrefManager
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.CartVariantModel
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.CartViewModel
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.cart_layout.*
import kotlinx.android.synthetic.main.cart_layout.deleteCart
import kotlinx.android.synthetic.main.cart_layout.orderNow
import kotlinx.android.synthetic.main.confirm_order_layout.*
import kotlinx.android.synthetic.main.success_layout.*

class CartFragment : Fragment(R.layout.cart_layout), CartAdapter.ClickListener {

    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: List<CartRoom>
    private lateinit var mainViewModel: MainViewModel
    private val TAG = "CartFragment"
    private lateinit var progressDialog: ProgressDialog
    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(this)
        val type = object : TypeToken<User>() {}.type
        user =
            Gson().fromJson(
                SharedPrefManager.getData(context, Constants.USER_DATA),
                type
            )

        progressDialog = ProgressDialog(context)
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        cartRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }
        noData.visibility = View.VISIBLE
        cartRecycler.visibility = View.GONE

        cartViewModel.cartList.observe(viewLifecycleOwner, {
            cartList = it
            if (cartList.isEmpty()) {
                noData.visibility = View.VISIBLE
                cartRecycler.visibility = View.GONE
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "onViewCreated: $cartList")
                noData.visibility = View.GONE
                cartRecycler.visibility = View.VISIBLE
                cartAdapter.differ.submitList(cartList)
            }
        })

        orderNow.setOnClickListener {
            if (cartList.isNotEmpty()) {

//                showSuccessDialog()
                if (UserHelper.isNetworkConnected(requireContext()))
                    showConfirmOrderDialog()
                else
                    Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        deleteCart.setOnClickListener {
            cartViewModel.deleteAll()
            Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmOrderDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.confirm_order_layout)
        val alert = builder.create()
        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert.show()
        alert.apply {
            val confirmOrderAdapter = ConfirmOrderAdapter()
            confirmOrderRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = confirmOrderAdapter
            }
            confirmOrderAdapter.differ.submitList(cartList)
            var price = 0
            cartList.forEach { cartRoom ->
                cartRoom.variants.forEach { variant ->
                    price += variant.finalPrice

                }
            }
            totalPrice.text = "Total Price - \u20B9 $price"
            confirmOrder.setOnClickListener {
                submitOrder()
                alert.dismiss()
            }
            cancelOrder.setOnClickListener {
                alert.dismiss()
            }
        }
    }

    private fun submitOrder() {
        val productList: HashMap<String, List<CartVariantModel>> = HashMap()
        cartList.forEach {
            val variantId: MutableList<CartVariantModel> = ArrayList()
            it.variants.forEach {
                variantId.add(CartVariantModel(it.variant_id, it.quantity))
            }
            productList[it.productId] = variantId
        }

        mainViewModel.orderProduct(Gson().toJson(productList), user.uid)
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        cancelLoading()
                        Toast.makeText(
                            context,
                            it.data?.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        if (!it.data?.body()?.error!!) {
                            showSuccessDialog()
                            cartViewModel.deleteAll()
                        }
                    }
                    is Resource.Failure -> {
                        cancelLoading()
                        Toast.makeText(
                            context,
                            it.data?.body()?.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.success_layout)
        val successDialog = builder.create()
        successDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        successDialog.show()
        successDialog.apply {
            animation.scale = 15f
        }
        Handler().postDelayed({
            successDialog.dismiss()
        }, 1500)
    }

    override fun deleteProduct(cartRoom: CartRoom) {
        cartViewModel.deleteProduct(cartRoom)
    }

    private fun showLoading() {
        progressDialog.setMessage("Placing Order...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun cancelLoading() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}
