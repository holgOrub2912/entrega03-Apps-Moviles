package com.freenando.compralista.ui

import com.freenando.compralista.data.ProductEntry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.freenando.compralista.ProductByEANQuery
import com.freenando.compralista.data.EntriesRepository
import com.freenando.compralista.data.SupermarketList
import kotlinx.coroutines.delay
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

class GroceryListViewModel(private val supermarketList: SupermarketList, private val entriesRepository: EntriesRepository) : ViewModel() {
    val uiState: StateFlow<ListUiState> = entriesRepository.getEntriesInSupermarketListStream(supermarketList.id).map { ListUiState(it) }

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILIS),
            initialValue = ListUiState()
        )
    private val _appInfo: MutableStateFlow<AppInfo> = MutableStateFlow(AppInfo.AWAITING_INPUT);
    val appInfo
        get() = _appInfo

    fun addProduct(ean: String) {
        /*
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://www.olimpica.com/_v/segment/graphql/v1")
            .build();
        _appInfo.update { AppInfo.LOADING }

        viewModelScope.launch {
            val response = apolloClient
                .query(ProductByEANQuery(ean = ean))
                .execute();
            if (response.data != null
                && response.data!!.product != null
            ) {
                if ( uiState.value.productAlreadyAdded(response.data!!.product?.productId!!) )
                    toggleAddedToCart(response.data!!.product?.productId!!)
                else
                    saveEntry(ProductEntry(response.data!!.product!!))
                _appInfo.update { AppInfo.AWAITING_INPUT }
                // stateIsLoading.update { false }
            } else {
                _appInfo.update { AppInfo.NETWORK_ERROR }
                delay(NETWORK_ERR_DELAY)
                _appInfo.update { AppInfo.AWAITING_INPUT }
            }
        }
        */
        viewModelScope.launch {
            val product = supermarketList.searcher.searchByEAN(ean)

            if ( uiState.value.productAlreadyAdded(product.id) )
                toggleAddedToCart(product.id)
            else
                saveEntry(ProductEntry(product, supermarketList.id))
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