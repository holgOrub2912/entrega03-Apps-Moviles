package com.freenando.ListaCompra.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.freenando.ListaCompra.MainActivity

class GroceryListViewModelProvider(supermarketListId: Int, context: MainActivity) {
    val Factory = viewModelFactory {
        initializer {
            GroceryListViewModel(supermarketListId, context.container.entriesRepository)
        }
    }
}