package com.freenando.ListaCompra.search

import com.freenando.ListaCompra.data.Product

enum class AllSupermarketSearcher: SupermarketSearcher {
    OLIMPICA{
        val olimpicaSearcher = OlimpicaSearcher()
        override fun getSupermarketImageRes(): Int =olimpicaSearcher.getSupermarketImageRes()
        override fun getSupermarketName() = olimpicaSearcher.getSupermarketName()
        override suspend fun searchByEAN(ean: String) = olimpicaSearcher.searchByEAN(ean)
    },
    EXITO {
        val exitoSearcher = ExitoSearcher()
        override fun getSupermarketImageRes(): Int = exitoSearcher.getSupermarketImageRes()
        override fun getSupermarketName(): String = exitoSearcher.getSupermarketName()
        override suspend fun searchByEAN(ean: String): Product = exitoSearcher.searchByEAN(ean)
    }
}