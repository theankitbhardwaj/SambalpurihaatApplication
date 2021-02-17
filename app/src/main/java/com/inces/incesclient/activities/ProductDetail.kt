package com.inces.incesclient.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inces.incesclient.R
import com.inces.incesclient.adapters.ProductDetailImageAdapter
import com.inces.incesclient.adapters.PromotedProductsAdapter
import com.inces.incesclient.adapters.VariantAdapter
import com.inces.incesclient.db.CartRoom
import com.inces.incesclient.fragments.CartFragment
import com.inces.incesclient.helpers.ProductHelper
import com.inces.incesclient.models.Image
import com.inces.incesclient.models.Product
import com.inces.incesclient.models.Variant
import com.inces.incesclient.viewmodels.CartViewModel
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.variants_bottom_layout.*


class ProductDetail : AppCompatActivity(), VariantAdapter.BreakDownListener {
    private lateinit var product: Product
    private lateinit var variantAdapter: VariantAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bottomLayout: BottomSheetDialog
    private lateinit var promotedProductsAdapter: PromotedProductsAdapter
    private val cartViewModel: CartViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        product = intent.extras?.get("Product") as Product

        cart.setOnClickListener {
            main.visibility = View.GONE
            frame.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction().replace(R.id.frame, CartFragment()).commit()
        }
        productTitle.text = product.title
        productDescription.text = product.description
        termsAndCond.text = product.terms_condition

        val allImages: MutableList<Image> = ArrayList()
        product.variants.forEach { variant ->
            variant.image.forEach {
                if (it.pos != "-1")
                    allImages.add(it)
            }
        }
        productVariant.text = "${product.variants.size} variants"
        productPrice.text = ProductHelper.setPriceRange(product.variants)
        productImages.setSliderAdapter(ProductDetailImageAdapter(allImages.sortedBy { it.pos }))



        if (checkIfAllVariantsAreOutOfStock()) {
            outOfStock.visibility = View.VISIBLE
            buyNow.text = "Out of Stock"
            buyNow.isEnabled = false
        } else {
            outOfStock.visibility = View.GONE
            buyNow.text = "Buy Now"
            buyNow.isEnabled = true
        }

        buyNow.setOnClickListener {
            showVariantBottomLayout()
        }

        cartViewModel.cartList.observe(this, {
            if (it.isEmpty())
                cartCount.visibility = View.GONE
            else {
                cartCount.visibility = View.VISIBLE
                cartCount.text = it.size.toString()
            }
        })

        showRelatedProducts()
    }

    private fun showRelatedProducts() {
        val listOfPromotedProducts: MutableList<Product> = ArrayList()

        promotedProductsAdapter = PromotedProductsAdapter()
        relatedProductRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@ProductDetail, LinearLayoutManager.HORIZONTAL, false)
            adapter = promotedProductsAdapter
        }

        productViewModel.allProducts.observe(this, { allProducts ->
            allProducts.forEach {
                if (product.sub_category == it.subCategory && product.product_id != it.productId)
                    listOfPromotedProducts.add(
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
            promotedProductsAdapter.differ.submitList(listOfPromotedProducts)
        })
    }


    private fun checkIfAllVariantsAreOutOfStock(): Boolean {
        var totalVariants = product.variants.size
        product.variants.forEach {
            if (it.quantity == 0)
                totalVariants--
        }
        return totalVariants == 0
    }

    private fun showVariantBottomLayout() {
        bottomLayout = BottomSheetDialog(this)
        bottomLayout.setContentView(R.layout.variants_bottom_layout)
        bottomLayout.dismissWithAnimation = true
        bottomLayout.show()
        bottomLayout.apply {
            variantAdapter = VariantAdapter(this@ProductDetail)
            productName.text = product.title
            variantRecycler.apply {
                layoutManager = LinearLayoutManager(this@ProductDetail)
                adapter = variantAdapter
            }
            variantAdapter.differ.submitList(product.variants)

            proceed.setOnClickListener {
                var size = variantAdapter.getUpdatedList().size
                val newVariantList: MutableList<Variant> = ArrayList()
                variantAdapter.getUpdatedList().forEach {
                    if (it.selectedQuantity == 0)
                        size--
                    if (size == 0) {
                        Toast.makeText(
                            this@ProductDetail,
                            "Add at least one variant to buy",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else if (it.selectedQuantity != 0) {
                        newVariantList.add(it)
                    }
                }
                val cartRoom = CartRoom(
                    product.product_id,
                    product.publish_date,
                    product.base_name,
                    product.terms_condition,
                    product.supplier_name,
                    product.title,
                    product.description,
                    product.main_category,
                    product.sub_category,
                    newVariantList
                )
                cartViewModel.insert(cartRoom)
                Toast.makeText(this@ProductDetail, "Added to cart", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    override fun updatePriceAndQuantity() {
        var quantity = 0
        var price = 0
        variantAdapter.getUpdatedList().forEach {
            quantity += it.selectedQuantity
            price += it.finalPrice
        }
        if (quantity == 0)
            bottomLayout.breakDownLayout.visibility = View.GONE
        else
            bottomLayout.breakDownLayout.visibility = View.VISIBLE
        bottomLayout.totalQuantity.text = "Total quantity : $quantity"
        bottomLayout.totalPrice.text = "\u20B9 $price"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}