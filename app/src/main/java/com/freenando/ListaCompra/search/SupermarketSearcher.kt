package com.freenando.ListaCompra.search

import androidx.annotation.DrawableRes
import com.freenando.ListaCompra.data.Product

interface SupermarketSearcher {
    @DrawableRes
    fun getSupermarketImageRes(): Int
    fun getSupermarketName(): String
    suspend fun searchByEAN(ean: String): Product?
}