package com.inces.incesclient.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.inces.incesclient.R
import com.inces.incesclient.adapters.DashboardProductAdapter
import com.inces.incesclient.adapters.SubCategoryAdapter
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.models.Product
import com.inces.incesclient.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_category.search
import kotlinx.android.synthetic.main.activity_category.subCategoryProductRecycler

class CategoryActivity : AppCompatActivity() {
    private lateinit var category: String
    private lateinit var price: String
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var products: List<ProductsDB>
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private lateinit var dashboardProductAdapter: DashboardProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        category = intent.extras?.get("category").toString()
        price = intent.extras?.get("price").toString()

        dashboardProductAdapter = DashboardProductAdapter()

        search.setOnClickListener {
            startActivity(Intent(this, Search::class.java))
        }

        subCategoryProductRecycler.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            adapter = dashboardProductAdapter
        }

        productViewModel.allProducts.observe(this, Observer {
            products = it
            if (price == "-1") {
                dashboardProductAdapter.differ.submitList(getSubCategoryProductList())
            } else {
                dashboardProductAdapter.differ.submitList(getFilteredProductList())
            }
        })
    }

    private fun getFilteredProductList(): MutableList<Product>? {
        val filteredList: MutableList<Product> = ArrayList()
        products.forEach {
            if (it.mainCategory == category) {
                val itemPrice = ProductHelper.getMinAndMaxRange(it.variants)
                if (itemPrice != null) {
                    if (itemPrice.contains(",")) {
                        val max = itemPrice.subSequence(itemPrice.indexOf(",")+1, itemPrice.length)
                            .toString().toInt()
                        when (price) {
                            "0" -> {
                                if (max < 599)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                            "599" -> {
                                if (max < 1999)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                            "1999" -> {
                                if (max > 1999)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                        }
                    } else {
                        val max = itemPrice.toString().toInt()
                        when (price) {
                            "0" -> {
                                if (max < 599)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                            "599" -> {
                                if (max < 1999)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                            "1999" -> {
                                if (max > 1999)
                                    filteredList.add(
                                        Product(
                                            it.baseName,
                                            it.description,
                                            it.mainCategory,
                                            it.productId,
                                            it.datePosted,
                                            it.subCategory,
                                            it.vendorName,
                                            it.termAndCondition,
                                            it.title,
                                            it.variants,
                                        )
                                    )
                            }
                        }
                    }
                }
            }
        }
        return filteredList
    }

    private fun getSubCategoryProductList(): MutableList<Product>? {
        val subCategory: MutableList<Product> = ArrayList()
        products.forEach {
            if (it.mainCategory == category)
                subCategory.add(
                    Product(
                        it.baseName,
                        it.description,
                        it.mainCategory,
                        it.productId,
                        it.datePosted,
                        it.subCategory,
                        it.vendorName,
                        it.termAndCondition,
                        it.title,
                        it.variants,
                    )
                )
        }
        return subCategory
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}