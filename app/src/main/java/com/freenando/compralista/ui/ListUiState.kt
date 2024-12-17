package com.freenando.compralista.ui

import com.freenando.compralista.data.ProductEntry

data class ListUiState (
    val list: List<ProductEntry> = listOf(),
){
    fun productAlreadyAdded(id: String): Boolean {
        return list.any {
            it.id == id
        }
    }
}
