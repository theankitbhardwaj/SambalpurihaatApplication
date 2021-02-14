package com.inces.incesclient.repo

import androidx.lifecycle.LiveData
import com.inces.incesclient.db.ProductDao
import com.inces.incesclient.db.ProductsDB
import com.inces.incesclient.models.Product

class ProductRepo(private val productDao: ProductDao) {

    val allProducts: LiveData<List<ProductsDB>> = productDao.getAll()

    fun insert(product: ProductsDB) {
        productDao.insertAll(products = product)
    }

    fun deleteAll() {
        productDao.deleteAll()
    }

    fun getSingleProduct(productId: String): List<ProductsDB> =
        productDao.getSingleProduct(productId)

}