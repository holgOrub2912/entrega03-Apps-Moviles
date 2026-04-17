package com.freenando.ListaCompra.search

import androidx.annotation.DrawableRes
import com.freenando.ListaCompra.data.Product
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.Query
import com.freenando.ListaCompra.R

@Serializable
data class ExitoOffer(
    val lowPrice: Int
)

@Serializable
data class ExitoProduct(
    val id: String,
    val name: String,
    val offers: ExitoOffer
)

@Serializable
data class ExitoSearchSuggestions(
    val products: List<ExitoProduct>
)

@Serializable
data class ExitoSubSearchResult(
    val suggestions: ExitoSearchSuggestions
)

@Serializable
data class ExitoSearchResults (
    val search: ExitoSubSearchResult
)

@Serializable
data class ExitoDataSearchResults (
    val data: ExitoSearchResults,
)

@Serializable
data class QueryVariables(
    val term: String,
    val first: Int = 1,
    val after: String = "0",
    val sort: String = "score_desc",
    val selectedFacets: List<String> = listOf()
)

public interface ExitoSearcherService {
    @GET("/api/graphql?operationName=SearchQuery")
    fun queryProduct(@Query("variables") vars: String) : Call<ExitoDataSearchResults>
}

class ExitoSearcher: SupermarketSearcher {
    private val SUPERMARKET_NAME = "Éxito"
    override fun getSupermarketName(): String = SUPERMARKET_NAME
    @DrawableRes
    override fun getSupermarketImageRes(): Int = R.drawable.exito

    override suspend fun searchByEAN(ean: String): Product {
        val defaultJson = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.exito.com")
            .addConverterFactory(defaultJson.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .build()
        val service = retrofit.create<ExitoSearcherService>()

        val stringVars = defaultJson.encodeToString(QueryVariables(term = ean))

        val result = service.queryProduct(stringVars).await()
        return Product(
            id = result.data.search.suggestions.products[0].id,
            name = result.data.search.suggestions.products[0].name,
            price = result.data.search.suggestions.products[0].offers.lowPrice.toDouble()
        )
    }
}