package com.freenando.ListaCompra.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.freenando.ListaCompra.R
import com.freenando.ListaCompra.data.EntriesRepository
import com.freenando.ListaCompra.data.SupermarketList
import com.freenando.ListaCompra.data.SupermarketListInfo
import com.freenando.ListaCompra.search.AllSupermarketSearcher
import com.freenando.ListaCompra.ui.theme.AppShapes
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
                       onDeleteList: (id: SupermarketList) -> Unit,
                       modifier: Modifier = Modifier
){
    val supermarketLists by repository.getSupermarketListStream().collectAsState(listOf())

    SupermarketLists(
        supermarketLists,
        onNavigateToNewList,
        onNavigateToExistingList,
        onNavigateToCompare,
        onDeleteList,
        modifier = modifier
    )
}

@Composable
fun SupermarketLists(
    supermarketLists: List<SupermarketListInfo>,
    onNavigateToNewList: () -> Unit,
    onNavigateToExistingList: (id: Int) -> Unit,
    onNavigateToCompare: () -> Unit,
    onDeleteList: (list: SupermarketList) -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_small))
    ) {
        Scaffold(floatingActionButton = {
            Row(modifier = Modifier) {
                NavigateToNewListScreenBtn(onClick = onNavigateToNewList)
            }
        }) {innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 50.dp)
                ) {
                    /// TODO: Logo should go here
                    Button(
                        onClick = onNavigateToCompare,
                        shape = MaterialTheme.shapes.small
                    ) { Text(stringResource(R.string.compare_price)) }
                }
                LazyColumn {
                    items(supermarketLists, key = {it.id}){supermarketList ->
                        Button(
                            onClick = { onNavigateToExistingList(supermarketList.id) },
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                disabledContainerColor = MaterialTheme.colorScheme.onError,
                                disabledContentColor = MaterialTheme.colorScheme.onError
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                Column(
                                    modifier = Modifier
                                ) {
                                    Text(
                                        supermarketList.name,
                                        fontSize = 20.sp,
                                        softWrap = true,
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                    ){
                                        Text(
                                            "${supermarketList.totalProducts.toString()} ${stringResource(if (supermarketList.totalProducts == 1) R.string.products_qty_indicator_singular else R.string.products_qty_indicator_plural)}",
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(30.dp))
                                        Text(
                                            currencyFormatter.format(supermarketList.totalPrice),
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                Row (modifier = Modifier.width(IntrinsicSize.Min)) {
                                    Image(
                                        painter = painterResource(supermarketList.searcher.getSupermarketImageRes()),
                                        contentDescription = supermarketList.searcher.getSupermarketName(),
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(shape = MaterialTheme.shapes.small)
                                            .padding(end = 4.dp)
                                    )
                                    if (supermarketList.totalProducts == 0)
                                        IconButton(
                                            onClick = { onDeleteList(SupermarketList(supermarketList)) },
                                            modifier = modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = MaterialTheme.shapes.small
                                                )
                                                .size(50.dp)
                                        ) {
                                            Icon(
                                                Icons.Rounded.Delete,
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                contentDescription =  stringResource(R.string.delete_list)
                                            )
                                        }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
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
                        totalPrice = 45000.0,
                        totalProducts = 1
                    ),
                    SupermarketListInfo(
                        id = 1,
                        name = "Mercado del exito",
                        searcher = AllSupermarketSearcher.EXITO,
                        totalPrice = 60000.0,
                        totalProducts = 5
                    ),
                    SupermarketListInfo(
                        id = 2,
                        name = "Drogería del exito",
                        searcher = AllSupermarketSearcher.EXITO,
                        totalPrice = 34000.0,
                        totalProducts = 0
                )
                ),
                onNavigateToNewList = {},
                onNavigateToExistingList = {_ -> },
                onDeleteList = {},
                onNavigateToCompare = {}
            )
        }
    }
}