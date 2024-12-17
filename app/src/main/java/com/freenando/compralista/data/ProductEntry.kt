package com.freenando.compralista.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.freenando.compralista.ProductByEANQuery

@Entity(tableName = "productEntries")
data class ProductEntry(
    @PrimaryKey
    val id: String,
    val name: String,
    val unitPrice: Double,
    val addedToCart: Boolean = false,
    val quantity: Int = 1) {

    val totalPrice
        get() = this.unitPrice * quantity

    constructor(product: ProductByEANQuery.Product) : this(
        id = product.productId!!,
        name = product.productName!!,
        unitPrice = product.priceRange?.sellingPrice?.highPrice!!
    )

}