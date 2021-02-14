package com.inces.incesclient.helpers

import com.inces.incesclient.models.Variant
import java.util.*

object ProductHelper {
    fun setPriceRange(variants: List<Variant>): CharSequence? {
        return when (variants.size) {
            1 -> "\u20B9 ${variants[0].price}"
            2 -> sortPriceRange(variants[0].price, variants[1].price)
            else -> {
                val sortedList = variants.sortedBy {
                    it.price
                }
                if (sortedList[0].price != sortedList[variants.size - 1].price)
                    "\u20B9 ${sortedList[0].price} - \u20B9 ${sortedList[variants.size - 1].price}"
                else
                    "\u20B9 ${sortedList[0].price}"
            }
        }
    }

    fun getMinAndMaxRange(variants: List<Variant>): CharSequence? {
        return when (variants.size) {
            1 -> "${variants[0].price}"
            2 -> sortPriceRange2(variants[0].price, variants[1].price)
            else -> {
                val sortedList = variants.sortedBy {
                    it.price
                }
                if (sortedList[0].price != sortedList[variants.size - 1].price)
                    "${sortedList[0].price},${sortedList[variants.size - 1].price}"
                else
                    "${sortedList[0].price}"
            }
        }
    }

    private fun sortPriceRange(price: Int, price1: Int): CharSequence? {
        return if (price == price1) "\u20B9 $price" else if (price < price1) "\u20B9 $price - \u20B9 $price1" else "\u20B9 $price1 - \u20B9 $price"
    }

    private fun sortDiscountRange(price: Int, price1: Int): CharSequence? {
        return if (price == 0 && price1 == 0) "" else if (price == 0) {
            if (price1 == 0) "" else "$price1% off"
        } else if (price1 == 0) {
            if (price == 0) "" else "$price% off"
        } else if (price < price1) "$price% - $price1% off" else "$price1% - $price% off"
    }


    private fun sortPriceRange2(price: Int, price1: Int): CharSequence? {
        return if (price == price1) "$price" else if (price < price1) "$price,$price1" else "$price1,$price"
    }

    fun getRandomNumber(totalSize: Int): Int {
        return Random().nextInt(totalSize)
    }

    fun getDiscountPrice(discount: Int, price: Int): CharSequence? {
        val temp = (discount * price) / 100
        return (price - temp).toString()
    }

    fun getDiscountPriceToInt(discount: Int, price: Int): Int? {
        val temp = (discount * price) / 100
        return (price - temp)
    }

    fun setDiscountRange(variants: List<Variant>): CharSequence? {
        return when (variants.size) {
            1 -> if (variants[0].discount == 0) "" else "${variants[0].discount}% off"
            2 -> sortDiscountRange(variants[0].discount, variants[1].discount)
            else -> {
                val sortedList = variants.sortedBy {
                    it.discount
                }
                if (sortedList[0].discount == 0 && sortedList[variants.size - 1].discount == 0) {
                    ""
                } else if (sortedList[0].discount == 0) {
                    if (sortedList[variants.size - 1].discount == 0)
                        ""
                    else
                        "${sortedList[variants.size - 1].discount}% off"
                } else if (sortedList[variants.size - 1].discount == 0) {
                    if (sortedList[0].discount == 0)
                        ""
                    else
                        "${sortedList[0].discount}% off"
                } else if (sortedList[0].discount != sortedList[variants.size - 1].discount) {
                    "${sortedList[0].discount}% - ${sortedList[variants.size - 1].discount}% off"
                } else
                    "${sortedList[0].discount}% off"
            }
        }
    }


}