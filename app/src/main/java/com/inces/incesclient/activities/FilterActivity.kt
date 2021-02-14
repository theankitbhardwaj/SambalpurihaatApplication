package com.inces.incesclient.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.inces.incesclient.R
import com.inces.incesclient.adapters.FilterAdapter
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.models.Product
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.ProductViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_product_detail.*

class FilterActivity : AppCompatActivity() {
    private lateinit var category: String
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var products: List<ProductsDB>
    private lateinit var categorizedProduct: MutableList<Product>
    private lateinit var supplierList: MutableList<String>
    private lateinit var mainViewModel: MainViewModel
    private lateinit var supplierProductList: MutableMap<String, MutableList<Product>>
    private lateinit var filterAdapter: FilterAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        category = intent.extras?.get("category").toString()
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        categorizedProduct = ArrayList()
        supplierProductList = HashMap()
        supplierList = ArrayList()

        below.setOnClickListener {
            val i = Intent(this@FilterActivity, CategoryActivity::class.java)
            i.putExtra("category", category)
            i.putExtra("price", "0")
            startActivity(i)
        }
        middle.setOnClickListener {
            val i = Intent(this@FilterActivity, CategoryActivity::class.java)
            i.putExtra("category", category)
            i.putExtra("price", "599")
            startActivity(i)
        }
        above.setOnClickListener {
            val i = Intent(this@FilterActivity, CategoryActivity::class.java)
            i.putExtra("category", category)
            i.putExtra("price", "1999")
            startActivity(i)
        }

        productViewModel.allProducts.observe(this, { it ->
            products = it
            if (products.isNotEmpty()) {
                products.forEach {
                    if (it.mainCategory == category)
                        categorizedProduct.add(
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
                categorizedProduct.forEach {
                    if (!supplierList.contains(it.supplier_name))
                        supplierList.add(it.supplier_name)
                }

                mainViewModel.promotedProduct().observe(this, {
                    when (it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            val supplierList: MutableList<String> = ArrayList()
                            it.data?.body()?.suppliers?.forEach {
                                supplierList.add(it.supplier_name)
                                for (i in supplierList) {
                                    val temp: MutableList<Product> = ArrayList()
                                    categorizedProduct.forEach {
                                        if (it.supplier_name == i) {
                                            temp.add(it)
                                        }
                                    }
                                    if (!temp.isNullOrEmpty())
                                        supplierProductList[i] = temp
                                }

                                Log.e("PromotedProduct", "onCreate: ${supplierProductList.size}")
                                filterAdapter = FilterAdapter(supplierList, supplierProductList)
                                promotedItemsRecycler.apply {
                                    layoutManager = LinearLayoutManager(this@FilterActivity)
                                    adapter = filterAdapter
                                }
                            }
                        }
                        is Resource.Failure -> {
                        }
                    }
                })

                Glide.with(this)
                    .load(Constants.IMAGE_URL + categorizedProduct[0].variants[0].image[0].img)
                    .into(productImage)

                noOfProducts.text = "${categorizedProduct.size} products"
                noOfSuppliers.text = "From ${supplierList.size} suppliers"

                allCard.setOnClickListener {
                    val i = Intent(this@FilterActivity, CategoryActivity::class.java)
                    i.putExtra("category", category)
                    i.putExtra("price", "-1")
                    startActivity(i)
                }
            } else Toast.makeText(this, "No Product Available", Toast.LENGTH_SHORT).show()
        })
    }
}