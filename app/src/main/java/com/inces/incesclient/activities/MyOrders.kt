package com.inces.incesclient.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.inces.incesclient.R
import com.inces.incesclient.adapters.MyOrderAdapter
import com.inces.incesclient.helpers.SharedPrefManager.getData
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.ProductViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.activity_my_orders.toolbar
import kotlinx.android.synthetic.main.activity_product_detail.*

class MyOrders : AppCompatActivity(), MyOrderAdapter.ClickListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: User
    private lateinit var progressDialog: ProgressDialog
    private lateinit var myOrderAdapter: MyOrderAdapter
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        progressDialog = ProgressDialog(this)
        val type = object : TypeToken<User>() {}.type
        user =
            Gson().fromJson<User>(
                getData(this, Constants.USER_DATA),
                type
            )

        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        myOrderAdapter = MyOrderAdapter(this)
        myOrdersRecycler.apply {
            layoutManager = LinearLayoutManager(this@MyOrders)
            adapter = myOrderAdapter
        }


        noData.visibility = View.VISIBLE
        myOrdersRecycler.visibility = View.GONE
        if (UserHelper.isNetworkConnected(this))
            getOrders()
        else
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT)
                .show()

    }

    private fun getOrders() {
        mainViewModel.getMyOrders(user.uid).observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    showLoading("Loading Products...")
                }
                is Resource.Success -> {
                    cancelLoading()
                    if (!it.data?.body()?.error!!) {
                        if (it.data.body()?.orders?.isNotEmpty()!!) {
                            myOrderAdapter.differ.submitList(it.data.body()?.orders!!.sortedByDescending { it.orderID })
                            noData.visibility = View.GONE
                            myOrdersRecycler.visibility = View.VISIBLE
                        } else {
                            noData.visibility = View.VISIBLE
                            myOrdersRecycler.visibility = View.GONE
                        }
                    }
                }
                is Resource.Failure -> {
                    cancelLoading()
                    Toast.makeText(this, "Connection Timed out", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun showLoading(message: String) {
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun cancelLoading() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }

    override fun cancelOrder(orderId: Int) {
        if (UserHelper.isNetworkConnected(this)) {
            mainViewModel.cancelOrder(orderId).observe(this, Observer {
                when (it) {
                    is Resource.Loading -> {
                        showLoading("Cancelling product...")
                    }
                    is Resource.Success -> {
                        cancelLoading()
                        if (!it.data?.body()?.error!!) {
                            Toast.makeText(this, "Order Cancelled Successfully", Toast.LENGTH_SHORT)
                                .show()
                            getOrders()
                        }
                    }
                    is Resource.Failure -> {
                        cancelLoading()
                        Toast.makeText(this, "Connection Timed out", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT)
                .show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}