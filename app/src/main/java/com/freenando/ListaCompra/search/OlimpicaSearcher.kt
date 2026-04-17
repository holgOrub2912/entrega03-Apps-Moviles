package com.freenando.ListaCompra.search

import androidx.annotation.DrawableRes
import com.apollographql.apollo.ApolloClient
import com.freenando.ListaCompra.ProductByEANQuery
import com.freenando.ListaCompra.R
import com.freenando.ListaCompra.data.Product

class OlimpicaSearcher: SupermarketSearcher {
    private val SUPERMARKET_NAME = "Olímpica"
    override fun getSupermarketName(): String = SUPERMARKET_NAME

    @DrawableRes
    override fun getSupermarketImageRes(): Int = R.drawable.olimpica

    override suspend fun searchByEAN(ean: String): Product {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://www.olimpica.com/_v/segment/graphql/v1")
            .build();

        val response = apolloClient
            .query(ProductByEANQuery(ean = ean))
            .execute()

        if (response.data != null && response.data!!.product != null)
            return Product(response.data!!.product!!)
        else
            throw IllegalArgumentException(ean)
    }

}