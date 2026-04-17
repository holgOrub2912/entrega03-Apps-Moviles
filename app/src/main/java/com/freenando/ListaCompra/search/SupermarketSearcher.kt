package com.freenando.ListaCompra.search

import com.freenando.ListaCompra.data.Product

interface SupermarketSearcher {
    fun getSupermarketName(): String
    suspend fun searchByEAN(ean: String): Product
}