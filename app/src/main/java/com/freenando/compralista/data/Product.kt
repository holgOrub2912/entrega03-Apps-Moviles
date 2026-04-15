package com.freenando.compralista.data

import androidx.room.Entity
import com.freenando.compralista.ProductByEANQuery

data class Product(
    val id: String,
    val name: String,
    val price: Double) {

    constructor(product: ProductByEANQuery.Product) : this(
        product.productId!!,
        product.productName!!,
        product.priceRange?.sellingPrice?.highPrice!!
        )

    override fun equals(other: Any?): Boolean {
        if (other !is Product)
            return super.equals(other)
        else
            return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}