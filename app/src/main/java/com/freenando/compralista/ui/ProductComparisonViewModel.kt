package com.freenando.compralista.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freenando.compralista.data.EntriesRepository
import com.freenando.compralista.data.Product
import com.freenando.compralista.data.SupermarketList
import com.freenando.compralista.data.SupermarketListInfo
import com.freenando.compralista.search.AllSupermarketSearcher
import com.freenando.compralista.search.SupermarketSearcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.map

data class ProductComparisonUiState(
    val list: List<Pair<SupermarketSearcher, Product>> = listOf()
){
    fun isEmpty(): Boolean = list.isEmpty()
}

class ProductComparisonViewModel(private val ean: String, private val searchers: List<SupermarketListInfo>): ViewModel() {
    private val _uiState: MutableStateFlow<ProductComparisonUiState> = MutableStateFlow(ProductComparisonUiState())
    val uiState: StateFlow<ProductComparisonUiState>
        get() {
            if (_uiState.value.isEmpty())
                loadProducts()
            return _uiState.asStateFlow()
        }


    fun loadProducts(){
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