package com.freenando.ListaCompra.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
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
import com.freenando.ListaCompra.data.Product
import com.freenando.ListaCompra.data.ProductEntry
import com.freenando.ListaCompra.data.SupermarketListInfo
import com.freenando.ListaCompra.search.AllSupermarketSearcher
import com.freenando.ListaCompra.search.SupermarketSearcher
import com.freenando.ListaCompra.ui.theme.CompraListaTheme
import java.net.UnknownHostException
import java.text.NumberFormat
import kotlin.math.exp

private val currencyFormatter = NumberFormat.getCurrencyInstance()

@Composable
fun ComparePriceScreen(
    ean: String,
    searchers: List<SupermarketListInfo>,
    modifier: Modifier = Modifier,
    onNavBack: () -> Unit,
    onAddToList: (String, Int) -> Unit
){
    val uiState by produceState(key1 = ean, key2 = searchers, initialValue = ProductComparisonUiState(
        ProductComparisonInfo.LOADING)){
        try {
            if (searchers.isEmpty())
                return@produceState
            val productPairs = searchers
                .distinctBy { it.searcher.getSupermarketName() }
                .map { Pair(it.searcher, it.searcher.searchByEAN(ean)) }
                .filter { it.second != null }
                .toList()
                .let {
                    value = ProductComparisonUiState(
                        info = ProductComparisonInfo.DONE,
                        list = it as List<Pair<SupermarketSearcher, Product>>
                    )
                }

        } catch (e: UnknownHostException) {
            value = ProductComparisonUiState(info = ProductComparisonInfo.DONE)
        }
    }

    Column (modifier = modifier
        .padding(
            vertical = dimensionResource(R.dimen.padding_small),
            horizontal = dimensionResource(R.dimen.padding_small)
        )
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
            ComparePriceList(
                ean,
                uiState,
                searchers,
                onAddToList = onAddToList,
                onNavBack = onNavBack,
                modifier = Modifier,
            )
        }
}

@Composable
fun ComparePriceList(
    ean: String,
    productPairs: ProductComparisonUiState,
    searchers: List<SupermarketListInfo>,
    onAddToList: (String, Int) -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
        ) {
            IconButton(
                onClick = onNavBack,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .width(70.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(R.string.navigate_back_main_menu),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        when {
            productPairs.info == ProductComparisonInfo.LOADING -> CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(top = 50.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            productPairs.info == ProductComparisonInfo.DONE && productPairs.list == null -> Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Warning,
                        contentDescription = stringResource(R.string.ic_error),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                    Text(
                        text = stringResource(R.string.err_network),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )

                }
            }

            productPairs.info == ProductComparisonInfo.DONE && productPairs.list!!.isEmpty() -> Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.ic_error),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )
                    Text(
                        text = stringResource(R.string.product_not_found),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    )

                }
            }


            productPairs.info == ProductComparisonInfo.DONE && productPairs.list!!.isNotEmpty() ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.product_found_on)
                                + " " + stringResource(
                                if (productPairs.list.size > 1)
                                        R.string.supermarkets_plural
                                    else
                                        R.string.supermarkets_singular
                                ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 30.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            productPairs.list,
                            key = {
                                Pair(
                                    it.first.getSupermarketName(),
                                    it.second.id.toString()
                                )
                            }) { pair ->
                            Column(
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = tween(durationMillis = 250),
                                        fadeOutSpec = tween(durationMillis = 250),
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessLow,
                                            dampingRatio = Spring.DampingRatioLowBouncy
                                        )
                                    )
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(dimensionResource(R.dimen.padding_small))
                                    ) {
                                        val matchingSupermarketLists = searchers
                                            .filter { it.searcher.getSupermarketName() == pair.first.getSupermarketName() }
                                        var listDropdownVisible by remember { mutableStateOf(false) }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(0.7f)
                                        ) {
                                            Text(pair.second.name, fontSize = 14.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                currencyFormatter.format(pair.second.price),
                                                fontSize = 18.sp
                                            )
                                        }
                                        Row {
                                            Image(
                                                painter = painterResource(pair.first.getSupermarketImageRes()),
                                                contentDescription = pair.first.getSupermarketName(),
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .clip(shape = MaterialTheme.shapes.small)
                                                    .padding(end = 4.dp)
                                            )
                                            Column {
                                                IconButton(
                                                    onClick = {
                                                        if (matchingSupermarketLists.size == 1) onAddToList(
                                                            ean,
                                                            matchingSupermarketLists[0].id
                                                        ) else listDropdownVisible = true
                                                    },
                                                    modifier = modifier
                                                        .background(
                                                            color = MaterialTheme.colorScheme.primary,
                                                            shape = MaterialTheme.shapes.small
                                                        )
                                                        .size(50.dp)
                                                ) {
                                                    Icon(
                                                        Icons.Rounded.Add,
                                                        tint = MaterialTheme.colorScheme.onPrimary,
                                                        contentDescription = stringResource(R.string.add_product_to_list)
                                                    )
                                                }
                                                DropdownMenu(
                                                    expanded = listDropdownVisible,
                                                    modifier = Modifier,
                                                    onDismissRequest = { listDropdownVisible = false }
                                                ) {
                                                    matchingSupermarketLists.map { supermarketListInfo ->
                                                        DropdownMenuItem(
                                                            modifier = Modifier,
                                                            text = { Text(supermarketListInfo.name) },
                                                            onClick = {
                                                                onAddToList(
                                                                    ean,
                                                                    supermarketListInfo.id
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            }
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
fun ComparePriceListPreview(){
    CompraListaTheme {
        Surface {
            ComparePriceList(
                "",
                ProductComparisonUiState(ProductComparisonInfo.DONE, listOf(
                    Pair(AllSupermarketSearcher.EXITO,
                        Product(
                        id = "2",
                        name = "Doritos de queso",
                        price = 3000.0,
                    )),
                    Pair(AllSupermarketSearcher.OLIMPICA,
                        Product(
                            id = "3",
                            name = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor",
                            price = 4000.0,
                        ),
                ))),
                searchers = listOf(),
                onNavBack = {},
                onAddToList = {_,_ ->},
            )
        }
        }
}