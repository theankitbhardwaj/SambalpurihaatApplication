package com.inces.incesclient.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.inces.incesclient.db.AppDatabase
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.repo.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepo
    val allProducts: LiveData<List<ProductsDB>>

    init {
        val productDao = AppDatabase.getProductDatabase(application).productDao()
        repository = ProductRepo(productDao)
        allProducts = repository.allProducts
    }

    fun insert(productsDB: ProductsDB) = viewModelScope.launch(Dispatchers.Default) {
        repository.insert(productsDB)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.Default) {
        repository.deleteAll()
    }

    fun getSingleProduct(productId: String) = repository.getSingleProduct(productId)

}