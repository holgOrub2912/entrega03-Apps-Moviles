package com.freenando.ListaCompra.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freenando.ListaCompra.R
import com.freenando.ListaCompra.data.EntriesRepository
import com.freenando.ListaCompra.data.SupermarketListInfo
import com.freenando.ListaCompra.search.AllSupermarketSearcher
import com.freenando.ListaCompra.ui.theme.CompraListaTheme
import java.text.NumberFormat


private val currencyFormatter = NumberFormat.getCurrencyInstance()
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
fun OverviewListScreen(repository: EntriesRepository,
                       onNavigateToNewList: () -> Unit,
                       onNavigateToExistingList: (id: Int) -> Unit,
                       onNavigateToCompare: () -> Unit,
                       modifier: Modifier = Modifier
){
    val supermarketLists by repository.getSupermarketListStream().collectAsState(listOf())

    SupermarketLists(
        supermarketLists,
        onNavigateToNewList,
        onNavigateToExistingList,
        onNavigateToCompare,
        modifier = modifier
    )
}

@Composable
fun SupermarketLists(
    supermarketLists: List<SupermarketListInfo>,
    onNavigateToNewList: () -> Unit,
    onNavigateToExistingList: (id: Int) -> Unit,
    onNavigateToCompare: () -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Scaffold(floatingActionButton = {
            Row(modifier = Modifier) {
                Button(onClick = onNavigateToCompare) { Text("Comparar precios") }
                NavigateToNewListScreenBtn(onClick = onNavigateToNewList)
            }
        }) {innerPadding ->
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)) {
                items(supermarketLists, key = {it.id}){supermarketList ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimensionResource(R.dimen.padding_small)),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(supermarketList.name)
                                Text(supermarketList.searcher.name)
                                IconButton(onClick = { onNavigateToExistingList(supermarketList.id)}) {
                                    Icon(
                                        Icons.AutoMirrored.Rounded.ArrowForward,
                                        contentDescription = stringResource(R.string.desc_btn_goto_list),
                                        modifier = Modifier
                                            .size(4.dp)
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 30.dp)
                            ){
                                Text(
                                    currencyFormatter.format(supermarketList.totalPrice),
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

}

@Preview
@Composable
fun SupermarketListsPreview(){
    CompraListaTheme {
        Surface {
            SupermarketLists(
                supermarketLists = listOf(
                    SupermarketListInfo(
                        id = 0,
                        name = "Cositas de La olímpica",
                        searcher = AllSupermarketSearcher.OLIMPICA,
                        totalPrice = 45000.0
                    ),
                    SupermarketListInfo(
                        id = 1,
                        name = "Mercado del exito",
                        searcher = AllSupermarketSearcher.EXITO,
                        totalPrice = 60000.0
                    )
                ),
                onNavigateToNewList = {},
                onNavigateToExistingList = {_ -> },
                onNavigateToCompare = {}
            )
        }
    }
}