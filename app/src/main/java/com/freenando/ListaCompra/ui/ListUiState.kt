package com.freenando.ListaCompra.ui

import com.freenando.ListaCompra.data.ProductEntry

data class ListUiState (
    val list: List<ProductEntry> = listOf(),
){
    fun productAlreadyAdded(id: String): Boolean {
        return list.any {
            it.id == id
        }
    }
}
