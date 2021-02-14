package com.inces.incesclient.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.inces.incesclient.db.AppDatabase
import com.inces.incesclient.db.CartRoom
import com.inces.incesclient.repo.CartRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CartRepo
    val cartList: LiveData<List<CartRoom>>

    init {
        val cartDao = AppDatabase.getCartDatabase(application).cartDao()
        repository = CartRepo(cartDao)
        cartList = repository.cartList
    }

    fun insert(cart: CartRoom) = viewModelScope.launch(Dispatchers.Default) {
        repository.insert(cart)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.Default) {
        repository.deleteAll()
    }

    fun deleteProduct(cart: CartRoom) = viewModelScope.launch(Dispatchers.Default) {
        repository.deleteProduct(cart)
    }
}