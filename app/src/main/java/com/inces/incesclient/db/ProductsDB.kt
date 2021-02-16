package com.inces.incesclient.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inces.incesclient.models.Variant


@Entity
data class ProductsDB(
    @PrimaryKey
    @ColumnInfo(name = "productId")
    var productId: String,

    @ColumnInfo(name = "datePosted")
    var datePosted: String,

    @ColumnInfo(name = "baseName")
    var baseName: String,

    @ColumnInfo(name = "termAndCondition")
    var termAndCondition: String,

    @ColumnInfo(name = "vendorName")
    var vendorName: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "mainCategory")
    var mainCategory: String,

    @ColumnInfo(name = "subCategory")
    var subCategory: String,

//    @ColumnInfo(name= "category")
//    var category: String,

    @ColumnInfo(name = "variants")
    var variants: List<Variant>
)