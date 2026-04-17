package com.freenando.compralista.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freenando.compralista.data.EntriesRepository
import com.freenando.compralista.data.SupermarketList
import com.freenando.compralista.data.SupermarketListInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    fun addList(list: SupermarketList){
        viewModelScope.launch {
            repository.insertSupermarketList(list)
        }
    }

    companion object {
        private const val TIMEOUT_MILIS = 5_000L
    }
}