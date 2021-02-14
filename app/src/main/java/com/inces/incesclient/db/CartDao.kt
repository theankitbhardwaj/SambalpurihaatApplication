package com.inces.incesclient.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CartDao {
    @Query("SELECT * FROM cartroom")
    fun getAll(): LiveData<List<CartRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cart: CartRoom)


    @Query("DELETE FROM cartroom")
    fun deleteAll()

    @Delete
    fun deleteProduct(cart: CartRoom)



}