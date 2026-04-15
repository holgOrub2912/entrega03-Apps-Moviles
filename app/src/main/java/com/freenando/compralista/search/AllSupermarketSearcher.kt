package com.freenando.compralista.search

import com.freenando.compralista.data.Product

enum class AllSupermarketSearcher: SupermarketSearcher {
    OLIMPICA{
        val olimpicaSearcher = OlimpicaSearcher()
        override fun getSupermarketName() = olimpicaSearcher.getSupermarketName()
        override suspend fun searchByEAN(ean: String) = olimpicaSearcher.searchByEAN(ean)
    },
    EXITO {
        val exitoSearcher = ExitoSearcher()
        override fun getSupermarketName(): String = exitoSearcher.getSupermarketName()
        override suspend fun searchByEAN(ean: String): Product = exitoSearcher.searchByEAN(ean)
    }
}