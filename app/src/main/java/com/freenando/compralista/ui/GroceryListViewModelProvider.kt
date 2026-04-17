package com.freenando.compralista.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.freenando.compralista.MainActivity
import com.freenando.compralista.data.SupermarketList

class GroceryListViewModelProvider(supermarketListId: Int, context: MainActivity) {
    val Factory = viewModelFactory {
        initializer {
            GroceryListViewModel(supermarketListId, context.container.entriesRepository)
        }
    }
}