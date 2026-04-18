package com.freenando.ListaCompra.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freenando.ListaCompra.data.EntriesRepository
import com.freenando.ListaCompra.data.Product
import com.freenando.ListaCompra.data.SupermarketList
import com.freenando.ListaCompra.data.SupermarketListInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SupermarketListsUiState(
    val list: List<SupermarketListInfo> = listOf()
)

class SupermarketListViewModel(private val repository: EntriesRepository): ViewModel() {
    val uiState: StateFlow<SupermarketListsUiState> = repository.getSupermarketListStream().map { SupermarketListsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILIS),
            initialValue = SupermarketListsUiState()
        )

    private val _productComparisonState: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    val productComparisonState: StateFlow<List<Product>> = _productComparisonState.asStateFlow()

    fun addList(list: SupermarketList){
        viewModelScope.launch {
            repository.insertSupermarketList(list)
        }
    }

    fun deleteList(list: SupermarketList){
        viewModelScope.launch {
            repository.deleteSupermarketList(list)
        }
    }

    companion object {
        private const val TIMEOUT_MILIS = 5_000L
    }
}