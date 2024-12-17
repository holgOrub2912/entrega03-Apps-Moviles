package com.freenando.compralista.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.freenando.compralista.MainActivity

class GroceryListViewModelProvider(context: MainActivity) {
    val Factory = viewModelFactory {
        initializer {
            GroceryListViewModel(context.container.entriesRepository)
        }
    }
}