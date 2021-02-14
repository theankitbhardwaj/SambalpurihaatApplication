package com.inces.incesclient.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ProductDao {
    @Query("SELECT * FROM productsdb")
    fun getAll(): LiveData<List<ProductsDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: ProductsDB)

    @Query("DELETE FROM productsdb")
    fun deleteAll()

    @Query("SELECT * FROM productsdb WHERE productId == :productId")
    fun getSingleProduct(productId: String): List<ProductsDB>
}