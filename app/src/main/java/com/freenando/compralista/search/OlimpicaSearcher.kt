package com.freenando.compralista.search

import com.freenando.compralista.data.Product

class OlimpicaSearcher: SupermarketSearcher {
    private val SUPERMARKET_NAME = "Olímpica"
    override fun getSupermarketName(): String = SUPERMARKET_NAME

    override suspend fun searchByEAN(ean: String): Product {
        TODO("Not yet implemented")
    }

}