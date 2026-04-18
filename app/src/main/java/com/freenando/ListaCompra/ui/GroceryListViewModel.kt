package com.freenando.ListaCompra.ui

import com.freenando.ListaCompra.data.ProductEntry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freenando.ListaCompra.data.EntriesRepository
import com.freenando.ListaCompra.data.SupermarketList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppInfo {
    AWAITING_INPUT,
    LOADING,
    NETWORK_ERROR,
}

class GroceryListViewModel(private val supermarketListId: Int, private val entriesRepository: EntriesRepository) : ViewModel() {
    val uiState: StateFlow<ListUiState> = entriesRepository.getEntriesInSupermarketListStream(supermarketListId).map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILIS),
            initialValue = ListUiState()
        )

    val supermarketListUiState: Flow<SupermarketList?> = entriesRepository.getSupermarketList(supermarketListId)
    private val _appInfo: MutableStateFlow<AppInfo> = MutableStateFlow(AppInfo.AWAITING_INPUT);
    val appInfo
        get() = _appInfo

    fun addProduct(ean: String) {
        viewModelScope.launch {
            try {
                supermarketListUiState.collect(
                    { supermarketList ->
                        _appInfo.value = AppInfo.LOADING
                        val product = supermarketList!!.searcher.searchByEAN(ean)
                        if (uiState.value.productAlreadyAdded(product.id))
                            toggleAddedToCart(product.id)
                        else
                            saveEntry(ProductEntry(product, supermarketListId))
                        _appInfo.value = AppInfo.AWAITING_INPUT
                    }
                )
            } catch (e: Exception) {
                _appInfo.value = AppInfo.NETWORK_ERROR
            }
        }
    }

    private suspend fun saveEntry(productEntry: ProductEntry){
        entriesRepository.insertEntry(productEntry);
    }

    private fun findEntry(id: String): ProductEntry {
        return uiState.value.list.first { it.id == id }
    }

    fun deleteProduct(id: String){
        _appInfo.update { AppInfo.AWAITING_INPUT }
        viewModelScope.launch {
            entriesRepository.deleteEntry(findEntry(id))
        }
    }

    fun toggleAddedToCart(id: String){
        viewModelScope.launch {
            findEntry(id).let{
                entriesRepository.updateEntry(it.copy(
                    addedToCart = !it.addedToCart
                ))
            }
        }
    }

    fun increaseProductQty(id: String, amount: Int){
        viewModelScope.launch {
            val productEntry = findEntry(id)
            if (-amount <= productEntry.quantity)
                entriesRepository.updateEntry(productEntry.copy(
                    quantity = amount + productEntry.quantity
                ))
        }
    }

    companion object {
        private const val TIMEOUT_MILIS = 5_000L
        private const val NETWORK_ERR_DELAY = 10_000L
    }
}