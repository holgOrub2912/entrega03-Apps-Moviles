package com.freenando.compralista.search

import com.freenando.compralista.data.Product

interface SupermarketSearcher {
    fun getSupermarketName(): String
    suspend fun searchByEAN(ean: String): Product
}