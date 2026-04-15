package com.freenando.compralista.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.freenando.compralista.ProductByEANQuery

@Entity(tableName = "productEntries")
data class ProductEntry(
    @PrimaryKey
    val id: String,
    val name: String,
    val unitPrice: Double,
    val superMarketListId: Int,
    val addedToCart: Boolean = false,
    val quantity: Int = 1) {

    val totalPrice
        get() = this.unitPrice * quantity

    constructor(product: Product, superMarketListId: Int) : this(
        id = product.id,
        name = product.name,
        unitPrice = product.price,
        superMarketListId = superMarketListId
    )

}