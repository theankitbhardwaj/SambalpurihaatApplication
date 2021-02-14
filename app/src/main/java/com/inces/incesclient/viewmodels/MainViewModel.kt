package com.inces.incesclient.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.inces.incesclient.models.User
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.util.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepo: MainRepo) : ViewModel() {

    fun createUser(user: User) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.createUser(user)))
        } catch (exception: Exception) {
            Log.e("MainViewModel", "createUser: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }

    fun getUser(uid: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getUser(uid)))
            Log.e("MainViewModel", "getUser: ${mainRepo.getUser(uid).body()}")
        } catch (exception: Exception) {
            Log.e("MainViewModel", "getUser: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }

    fun getOtp(phone_no: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getOtp(phone_no)))
            Log.e("MainViewModel", "getOtp: ${mainRepo.getOtp(phone_no)}")
        } catch (exception: Exception) {
            Log.e("MainViewModel", "getOtp: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }

    fun getAllProducts() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getAllProducts()))
        } catch (exception: Exception) {
            Log.e("MainViewModel", "getAllProducts: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }

    fun getAllCategory() = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(data = mainRepo.getAllCategory()))
        } catch (exception: Exception) {
            Log.e("MainViewModel", "getAllCategory: $exception")
            emit(exception.message?.let { Resource.Failure(data = null, message = it) })
        }
    }


    fun orderProduct(productList: String, uid: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(data = mainRepo.orderProduct(productList, uid)))
            } catch (exception: Exception) {
                Log.e("MainViewModel", "orderProduct: $exception")
                emit(exception.message?.let { Resource.Failure(data = null, message = it) })
            }
        }

    fun getMyOrders(uid: String) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(data = mainRepo.getMyOrders(uid)))
            } catch (exception: Exception) {
                Log.e("MainViewModel", "getMyOrders: $exception")
                emit(exception.message?.let { Resource.Failure(data = null, message = it) })
            }
        }

    fun cancelOrder(orderId: Int) =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(data = mainRepo.cancelOrder(orderId)))
            } catch (exception: Exception) {
                Log.e("MainViewModel", "cancelOrder: $exception")
                emit(exception.message?.let { Resource.Failure(data = null, message = it) })
            }
        }

    fun promotedProduct() =
        liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(Resource.Success(data = mainRepo.promotedProducts()))
            } catch (exception: Exception) {
                Log.e("MainViewModel", "cancelOrder: $exception")
                emit(exception.message?.let { Resource.Failure(data = null, message = it) })
            }
        }


}