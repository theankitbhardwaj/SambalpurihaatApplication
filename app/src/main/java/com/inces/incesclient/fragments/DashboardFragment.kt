package com.inces.incesclient.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inces.incesclient.R
import com.inces.incesclient.adapters.CategoryAdapter
import com.inces.incesclient.adapters.DashboardProductAdapter
import com.inces.incesclient.adapters.ImageSliderAdapter
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.helpers.UserHelper
import com.inces.incesclient.models.CategoryItem
import com.inces.incesclient.models.Product
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Constants
import com.inces.incesclient.util.Resource
import com.inces.incesclient.viewmodels.MainViewModel
import com.inces.incesclient.viewmodels.ProductViewModel
import com.inces.incesclient.viewmodels.factory.ViewModelFactory
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.dashboard_fragment_layout.*
import kotlinx.android.synthetic.main.filter_layout.*


class DashboardFragment : Fragment(R.layout.dashboard_fragment_layout) {


    private lateinit var productAdapter: DashboardProductAdapter
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var productsList: List<Product>
    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var vendorNameList: MutableList<String>
    private lateinit var productCategory: MutableList<String>
    private lateinit var categoryAdapter: CategoryAdapter
    private val TAG: String = "DashboardFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(context)

        if (UserHelper.isNetworkConnected(requireContext())) {
            setUpFeaturedProducts()
            setUpImageSlider()
        } else {
            Toast.makeText(
                context,
                Constants.NO_INTERNET,
                Toast.LENGTH_SHORT
            ).show()
        }

        /*filterBtn.setOnClickListener {
            showBottomDialog()
        }*/

        swipeUp.setOnRefreshListener {
            setUpFeaturedProducts()
            swipeUp.isRefreshing = false
        }


    }


    private fun showBottomDialog() {

        var new = false
        var trending = false
        var bHL = false
        var bLH = false

        var vendorName: String? = null

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.filter_layout)
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        bottomSheetDialog.apply {
            dismissWithAnimation = true

            val spinnerAdapter =
                ArrayAdapter(context, R.layout.spinner_text, vendorNameList)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            vendorNameSpinner.adapter = spinnerAdapter

            vendorNameSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    vendorName = vendorNameList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    vendorName = null
                }
            }

            newProducts.setOnClickListener {
                trendingProducts.background =
                    context.resources.getDrawable(R.drawable.primary_color_btn)
                trendingProducts.setTextColor(context.resources.getColor(android.R.color.white))
                trending = false

                if (new) {
                    newProducts.background =
                        context.resources.getDrawable(R.drawable.primary_color_btn)
                    newProducts.setTextColor(context.resources.getColor(android.R.color.white))
                    new = false
                } else {
                    newProducts.background = context.resources.getDrawable(R.drawable.secondary_btn)
                    newProducts.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    new = true
                }
            }
            trendingProducts.setOnClickListener {
                newProducts.background = context.resources.getDrawable(R.drawable.primary_color_btn)
                newProducts.setTextColor(context.resources.getColor(android.R.color.white))
                new = false

                if (trending) {
                    trendingProducts.background =
                        context.resources.getDrawable(R.drawable.primary_color_btn)
                    trendingProducts.setTextColor(context.resources.getColor(android.R.color.white))
                    trending = false
                } else {
                    trendingProducts.background =
                        context.resources.getDrawable(R.drawable.secondary_btn)
                    trendingProducts.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    trending = true
                }
            }

            highToLow.setOnClickListener {
                lowToHigh.background =
                    context.resources.getDrawable(R.drawable.primary_color_btn)
                lowToHigh.setTextColor(context.resources.getColor(android.R.color.white))
                bLH = false

                if (bHL) {
                    highToLow.background =
                        context.resources.getDrawable(R.drawable.primary_color_btn)
                    highToLow.setTextColor(context.resources.getColor(android.R.color.white))
                    bHL = false
                } else {
                    highToLow.background = context.resources.getDrawable(R.drawable.secondary_btn)
                    highToLow.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    bHL = true
                }
            }
            lowToHigh.setOnClickListener {
                highToLow.background = context.resources.getDrawable(R.drawable.primary_color_btn)
                highToLow.setTextColor(context.resources.getColor(android.R.color.white))
                bHL = false

                if (bLH) {
                    lowToHigh.background =
                        context.resources.getDrawable(R.drawable.primary_color_btn)
                    lowToHigh.setTextColor(context.resources.getColor(android.R.color.white))
                    bLH = false
                } else {
                    lowToHigh.background =
                        context.resources.getDrawable(R.drawable.secondary_btn)
                    lowToHigh.setTextColor(context.resources.getColor(R.color.colorPrimary))
                    bLH = true
                }
            }




            apply.setOnClickListener {
                Log.e(TAG, "Filter Parameters: and $vendorName ")
                dismiss()
                showFilteredProducts(vendorName, new, trending, bHL, bLH)
            }

            close.setOnClickListener {
                dismiss()
            }
            create()
            show()
        }
    }

    private fun showFilteredProducts(
        vendorName: String?,
        new: Boolean,
        trending: Boolean,
        bHL: Boolean,
        bLH: Boolean
    ) {
        vendorName?.let {
            showLoading()

            if (trending)
                Toast.makeText(context, "No Trending products available", Toast.LENGTH_SHORT).show()

            if (productsList.isNotEmpty()) {
                val filteredProducts: MutableList<Product> = ArrayList()
                for (product in productsList) {
                    if (product.supplier_name == it && !filteredProducts.contains(product))
                        filteredProducts.add(product)
                }
                if (new) {
                    for (product in filteredProducts) {
                        if (!UserHelper.isNewProduct(product.publish_date))
                            filteredProducts.remove(product)
                    }
                }
                productAdapter.differ.submitList(filteredProducts)
            } else
                Toast.makeText(context, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show()



            cancelLoading()
        }
    }


    private fun setUpImageSlider() {
        val images: MutableList<String> = ArrayList()
        images.add("https://sambalpurihaat.com/admin/images/banners/app_banner1.jpg")
        images.add("https://sambalpurihaat.com/admin/images/banners/app_banner2.jpg")
        images.add("https://sambalpurihaat.com/admin/images/banners/app_banner3.jpg")
        images.add("https://sambalpurihaat.com/admin/images/banners/app_banner4.jpg")
        images.add("https://sambalpurihaat.com/admin/images/banners/app_banner5.jpg")
        imageSliderAdapter = ImageSliderAdapter(images)
        imageSlider.setSliderAdapter(imageSliderAdapter)
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        imageSlider.scrollTimeInSec = 2
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        imageSlider.startAutoCycle()
    }

    private fun setUpFeaturedProducts() {
        productAdapter = DashboardProductAdapter()
        categoryAdapter = CategoryAdapter()
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MainRepo())
        ).get(MainViewModel::class.java)

        mainViewModel.getAllProducts().observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                }
                is Resource.Success -> {
                    cancelLoading()

                    val productViewModel: ProductViewModel by viewModels()
                    productViewModel.deleteAll()
                    productsList = response.data?.body()?.products!!
                    productAdapter.differ.submitList(response.data.body()?.products)
                    vendorNameList = ArrayList()
                    productCategory = ArrayList()
                    vendorNameList.add(0, "Select Supplier Name")
                    for (product in response.data.body()?.products!!) {
                        if (!vendorNameList.contains(product.supplier_name))
                            vendorNameList.add(product.supplier_name)

                        if (!productCategory.contains(product.main_category) and product.main_category.isNotBlank())
                            productCategory.add(product.main_category)

                        productViewModel.insert(
                            ProductsDB(
                                title = product.title,
                                datePosted = product.publish_date,
                                description = product.description,
                                productId = product.product_id,
                                variants = product.variants,
                                vendorName = product.supplier_name,
                                mainCategory = product.main_category,
                                subCategory = product.sub_category,
                                baseName = product.base_name,
                                termAndCondition = product.terms_condition
                            )
                        )
                    }
                    mainViewModel.getAllCategory().observe(viewLifecycleOwner, Observer { result ->
                        when (result) {
                            is Resource.Loading -> {
                            }
                            is Resource.Success -> {
                                val categoryList: MutableList<CategoryItem> = ArrayList()
                                result.data?.body()?.MainCategory?.let {
                                    it.forEach { item ->
                                        //if (!productCategory.contains(item.category_name))
                                            categoryList.add(item)
                                    }
                                    categoryAdapter.differ.submitList(categoryList)
                                }
                                categoryRecycler.apply {
                                    layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    adapter = categoryAdapter
                                }

                            }
                            is Resource.Failure -> {
                            }
                        }
                    })
                    featuredProductRecycler.apply {
                        layoutManager =
                            GridLayoutManager(context, 2)
                        adapter = productAdapter
                    }
                    featuredProductRecycler.isNestedScrollingEnabled = false
                }
                is Resource.Failure -> {
                    cancelLoading()

                }
            }

        })


    }

    private fun showLoading() {
        progressDialog.setMessage("Loading Products...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun cancelLoading() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }


}