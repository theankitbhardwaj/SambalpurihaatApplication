package com.inces.incesclient.repo

import androidx.lifecycle.LiveData
import com.inces.incesclient.db.CartDao
import com.inces.incesclient.db.CartRoom
import com.inces.incesclient.db.ProductsDB

class CartRepo(private val cartDao: CartDao) {
    val cartList: LiveData<List<CartRoom>> = cartDao.getAll()

    fun insert(cart: CartRoom) {
        cartDao.insertAll(cart)
    }

    fun deleteAll() {
        cartDao.deleteAll()
    }

    fun deleteProduct(cart: CartRoom) {
        cartDao.deleteProduct(cart)
    }
}
