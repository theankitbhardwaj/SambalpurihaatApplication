package com.inces.incesclient.activities

import android.os.Bundle
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

class Level3CategoryActivity : AppCompatActivity() {
    private lateinit var dashboardProductAdapter: DashboardProductAdapter
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var category: String
    private lateinit var products: List<ProductsDB>
    private lateinit var subCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level3_category)
        dashboardProductAdapter = DashboardProductAdapter()
        subCategory = intent.extras?.get("subCategory") as String
        category = intent.extras?.get("category") as String
        subCategoryProductRecycler.apply {
            layoutManager = GridLayoutManager(this@Level3CategoryActivity, 2)
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
            if (it.subCategory == subCategory && it.category == category)
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
                        it.category,
                        it.variants,
                    )
                )
        }
        return list
    }
}