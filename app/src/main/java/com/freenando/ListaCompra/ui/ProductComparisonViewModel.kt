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

data class ProductComparisonUiState(
    val list: List<Pair<SupermarketSearcher, Product>> = listOf()
){
    fun isEmpty(): Boolean = list.isEmpty()
}

class ProductComparisonViewModel(private val ean: String, private val searchers: List<SupermarketListInfo>): ViewModel() {
    private val _uiState: MutableStateFlow<ProductComparisonUiState>
        = MutableStateFlow(ProductComparisonUiState())
    val uiState: StateFlow<ProductComparisonUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = ProductComparisonUiState(searchers
                .map { it.searcher}
                .toSet()
                .map { Pair(it, it.searchByEAN(ean)) }
                .toList())
        }
    }

    companion object {
        private const val TIMEOUT_MILIS = 5_000L
    }
}