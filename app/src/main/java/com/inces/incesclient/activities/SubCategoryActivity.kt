package com.inces.incesclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.inces.incesclient.R
import com.inces.incesclient.adapters.DashboardProductAdapter
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.models.Product
import com.inces.incesclient.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.activity_sub_category.search

class SubCategoryActivity : AppCompatActivity() {
    private lateinit var dashboardProductAdapter: DashboardProductAdapter
    private lateinit var mainCategory: String
    private lateinit var products: List<ProductsDB>
    private lateinit var subCategory: String
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        mainCategory = intent.extras?.get("mainCategory") as String
        subCategory = intent.extras?.get("subCategory") as String

        search.setOnClickListener {
            startActivity(Intent(this, Search::class.java))
        }
        dashboardProductAdapter = DashboardProductAdapter()
        subCategoryProductRecycler.apply {
            layoutManager = GridLayoutManager(this@SubCategoryActivity, 2)
            adapter = dashboardProductAdapter
        }

        productViewModel.allProducts.observe(this, Observer {
            products = it
            dashboardProductAdapter.differ.submitList(getSubCategoryProductList())
        })
    }

    private fun getSubCategoryProductList(): MutableList<Product>? {
        val list: MutableList<Product> = ArrayList()
        products.forEach {
            if (it.mainCategory == mainCategory && it.subCategory == subCategory)
                list.add(
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
        return list
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}