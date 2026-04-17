package com.freenando.compralista

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.freenando.compralista.data.EntriesRepository
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

sealed class AppScreen(val route: String, @StringRes val title: Int){
    data object Start: AppScreen("home", R.string.app_name)
    data object NewList: AppScreen("addlist", R.string.add_supermarket_list)
    data object ComparePrices: AppScreen("compare/{productId}", R.string.add_supermarket_list) {
        fun createRoute(productId: Int): String = "compare/$productId"
    }
    data object ProductList: AppScreen("list/{supermarketListId}", R.string.supermarket_list){
        fun createRoute(supermarketListId: Int): String = "list/$supermarketListId"
    }
}

@Composable
fun NavigateToNewListScreenBtn(onClick: () -> Unit, modifier: Modifier = Modifier){
    IconButton(onClick = onClick,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small)
            .size(60.dp)
    ) {
        Icon(
            Icons.Rounded.Add,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(R.string.add_supermarket_list),
        )
    }
}

@Composable
fun OverviewListScreen(repository: EntriesRepository, onNavigateToNewList: () -> Unit, onNavigateToExistingList: (id: Int) -> Unit, modifier: Modifier = Modifier){
    val supermarketLists by repository.getSupermarketListStream().collectAsState(listOf())

    Column(
        modifier = modifier
    ) {
        Scaffold(floatingActionButton = { NavigateToNewListScreenBtn(onClick = onNavigateToNewList) }) {innerPadding ->
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)) {
                items(supermarketLists, key = {it.id}){supermarketList ->
                    Row() {
                        Text(supermarketList.name)
                        Text(supermarketList.searcher.name)
                        Button(onClick = { onNavigateToExistingList(supermarketList.id)}) {
                            Text("Go to list")
                        }
                    }
                }
            }
        }
    }

}