package com.freenando.ListaCompra.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freenando.ListaCompra.data.Product
import com.freenando.ListaCompra.data.SupermarketListInfo
import com.freenando.ListaCompra.search.SupermarketSearcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.map

enum class ProductComparisonInfo {
    LOADING,
    DONE,
    ERROR,
    NONE_FOUND,
}

data class ProductComparisonUiState(
    val info: ProductComparisonInfo,
    val list: List<Pair<SupermarketSearcher, Product>> = listOf(),
){
    fun isEmpty(): Boolean = list.isEmpty()
}