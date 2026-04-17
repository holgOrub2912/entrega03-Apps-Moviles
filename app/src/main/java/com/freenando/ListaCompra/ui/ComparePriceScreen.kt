package com.freenando.ListaCompra.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.freenando.ListaCompra.data.SupermarketListInfo
import java.text.NumberFormat

private val currencyFormatter = NumberFormat.getCurrencyInstance()

@Composable
fun ComparePriceScreen(
    ean: String,
    searchers: List<SupermarketListInfo>,
    modifier: Modifier = Modifier,
    productComparisonViewModel: ProductComparisonViewModel = ProductComparisonViewModel(ean, searchers),
    onAddToList: (String, Int) -> Unit
){

    val productPairs by productComparisonViewModel.uiState.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(productPairs.list, key = {Pair(it.first.getSupermarketName(), it.second.id.toString())}){pair ->
            Column {
                val supermarketList = searchers
                    .first {it.searcher.getSupermarketName() == pair.first.getSupermarketName()}
                Row {
                    Text(pair.first.getSupermarketName())
                    Text(pair.second.name)
                    Text(currencyFormatter.format(pair.second.price))
                }
                Button (onClick = { onAddToList(ean, supermarketList.id) }){
                    Text("Agregar a la lista")
                }
            }
        }
    }
}