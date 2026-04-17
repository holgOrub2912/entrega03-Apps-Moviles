package com.freenando.compralista.ui

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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.freenando.compralista.data.Product
import com.freenando.compralista.data.SupermarketListInfo
import com.freenando.compralista.search.AllSupermarketSearcher
import com.freenando.compralista.search.SupermarketSearcher
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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