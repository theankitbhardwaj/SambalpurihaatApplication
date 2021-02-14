package com.inces.incesclient.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.inces.incesclient.R
import com.inces.incesclient.adapters.DashboardProductAdapter
import com.inces.incesclient.models.Product
import com.inces.incesclient.util.Constants
import com.inces.incesclient.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import kotlin.collections.ArrayList

class Search : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var productList: MutableList<Product>
    private lateinit var dashboardProductAdapter: DashboardProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        productList = ArrayList()
        productViewModel.allProducts.observe(this, {
            for (db in it) {
                productList.add(
                    Product(
                        db.baseName,
                        db.description,
                        db.mainCategory,
                        db.productId,
                        db.datePosted,
                        db.subCategory,
                        db.vendorName,
                        db.termAndCondition,
                        db.title,
                        db.variants,
                    )
                )
                Log.e("TAG", "onCreate: $db")
            }
        })

        dashboardProductAdapter = DashboardProductAdapter()
        searchRecycler.apply {
            layoutManager = LinearLayoutManager(this@Search)
            adapter = dashboardProductAdapter
        }

        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchAndDisplay(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun searchAndDisplay(searchString: String?) {
        searchString?.let {
            if (productList.isNotEmpty()) {
                val localList: MutableList<Product> = ArrayList()
                for (product in productList) {
                    if (((product.title.toLowerCase(Locale.ROOT) == searchString) or product.title.toLowerCase(
                            Locale.ROOT
                        ).contains(searchString)) and !localList.contains(product)
                    )
                        localList.add(product)
                    Log.e(
                        "TAG",
                        "searchAndDisplay: $searchString and ${product.title.toLowerCase(Locale.ROOT)}"
                    )
                }
                dashboardProductAdapter.differ.submitList(localList)
            } else {
                Toast.makeText(
                    this,
                    Constants.NO_INTERNET,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}