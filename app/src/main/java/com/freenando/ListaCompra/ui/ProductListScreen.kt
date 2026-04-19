package com.freenando.ListaCompra.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freenando.ListaCompra.MainActivity
import com.freenando.ListaCompra.R
import com.freenando.ListaCompra.data.ProductEntry
import com.freenando.ListaCompra.ui.theme.CompraListaTheme
import java.text.NumberFormat


private val currencyFormatter = NumberFormat.getCurrencyInstance()

@Composable
fun ProductListScreen(supermarketListId: Int, context: MainActivity, newProductEan: String? = null, onNavBack: () -> Unit, modifier: Modifier = Modifier) {
    val groceryListViewModel = viewModel<GroceryListViewModel>(factory = GroceryListViewModelProvider(supermarketListId, context).Factory)
    val groceryListUiState by groceryListViewModel.uiState.collectAsState();
    val supermarketListUiState by groceryListViewModel.supermarketListUiState.collectAsState(null)
    val appInfo by groceryListViewModel.appInfo.collectAsState()
    val layoutDirection = LocalLayoutDirection.current;

    if (newProductEan != null)
        groceryListViewModel.addProduct(newProductEan)

    context.scanProductAction = {
        groceryListViewModel.addProduct(it)
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection),
            ),
    ){
        Scaffold(
            topBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 30.dp, bottom = 40.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                ) {
                    supermarketListUiState?.let { Text(it.name) }
                    IconButton (
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
            },
            bottomBar = {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 30.dp, horizontal = 20.dp)
                        .fillMaxWidth()
                ) {
                    TotalPriceBox(
                        cartPrice = groceryListUiState.list
                            .filter { it.addedToCart }
                            .sumOf  { it.totalPrice },
                        totalPrice = groceryListUiState.list
                            .sumOf{ it.totalPrice },
                    )
                    ScanBtn(onClick = { context.scanBarcode() })
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            ){
                ProductList(
                    onProductDeletion = { groceryListViewModel.deleteProduct(it) },
                    onProductIncQty = { id: String, amount: Int ->
                        groceryListViewModel.increaseProductQty(id, amount)
                    },
                    onToggleProdAddedToCart = { groceryListViewModel.toggleAddedToCart(it) },
                    groceryList = groceryListUiState.list,
                    appInfo = appInfo
                );
                AppInfoBox(appInfo = appInfo)
            }
        }
    }
}

@Composable
fun AppInfoBox(appInfo: AppInfo,
               modifier: Modifier = Modifier){
    Column (modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (appInfo == AppInfo.LOADING)
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        else if (appInfo == AppInfo.NETWORK_ERROR)
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row (verticalAlignment = Alignment.CenterVertically) {
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
        else if (appInfo == AppInfo.NOT_FOUND)
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row (verticalAlignment = Alignment.CenterVertically) {
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
    }
}

@Composable
@Preview
fun AppInfoBoxPreview(){
    Column {
        AppInfoBox(AppInfo.AWAITING_INPUT)
        AppInfoBox(AppInfo.LOADING)
        AppInfoBox(AppInfo.NETWORK_ERROR)
    }
}

@Composable
fun ScanBtn(onClick: () -> Unit,
            modifier: Modifier = Modifier){
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small)
            .size(60.dp)

    ){
        Icon(
            Icons.Rounded.Add,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = stringResource(R.string.desc_btn_scan),
        )
    }

}

@Composable
fun SmallBtnIcon(onClick: () -> Unit,
                 backgroundColor: Color,
                 icon: ImageVector,
                 description: String,
                 modifier: Modifier = Modifier){
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(color = backgroundColor,
                shape = MaterialTheme.shapes.small)
            .size(40.dp)

    ){
        Icon(
            icon,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = description,
        )
    }
}

@Composable
fun ProductEntryCard(productEntry: ProductEntry,
                     onToggleProdAddedToCart: (String) -> Unit,
                     onProductIncQty: (String, Int) -> Unit,
                     onProductDeletion: (String) -> Unit,
                     modifier: Modifier = Modifier){
    Card(modifier = modifier.fillMaxWidth()){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = productEntry.name,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (productEntry.addedToCart)
                        TextDecoration.LineThrough
                    else
                        null,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                )
                Row {
                    SmallBtnIcon(
                        onClick = {onToggleProdAddedToCart(productEntry.id)},
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Rounded.ShoppingCart,
                        description = stringResource(R.string.desc_btn_add_to_cart),
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
                    )
                    SmallBtnIcon(
                        onClick = { onProductDeletion(productEntry.id) },
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        icon = Icons.Rounded.Delete,
                        description = stringResource(R.string.desc_btn_delete)
                    )
                }
            }
            Row (
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                ProductQuantityBox(
                    quantity = productEntry.quantity,
                    onProductIncQty = { onProductIncQty(productEntry.id, it) },
                )
                AnimatedContent(
                    targetState = productEntry.totalPrice,
                    label = stringResource(R.string.label_product_price)
                ){
                    Text(text = currencyFormatter.format(it),
                        style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun ProductQuantityBox(quantity: Int,
                       onProductIncQty: (Int) -> Unit,
                       modifier: Modifier = Modifier){
    Row (modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        SmallBtnIcon(
            onClick = { onProductIncQty(-1) },
            backgroundColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Rounded.KeyboardArrowDown,
            description = stringResource(R.string.desc_btn_dec_qty),
        )
        AnimatedContent(
            targetState = quantity,
            label = stringResource(R.string.label_product_quantity),
            // Shamelessly copied from https://developer.android.com/develop/ui/compose/animation/composables-modifiers#animatedcontent
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState < initialState) {
                    // If the target number is larger, it slides from top and fades in
                    // while the initial (smaller) number slides down and fades out.
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )}
        ){ quantity ->
            Text(
                text = quantity.toString(),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                style = MaterialTheme.typography.labelSmall
            )

        }
        SmallBtnIcon(
            onClick = { onProductIncQty(1) },
            backgroundColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Rounded.KeyboardArrowUp,
            description = stringResource(R.string.desc_btn_inc_qty),
        )
    }
}

@Composable
fun TotalPriceBox(cartPrice: Double,
                  totalPrice: Double,
                  modifier: Modifier = Modifier){
    Row(modifier = modifier) {
        if (cartPrice > 0) {
            Text(
                text = currencyFormatter.format(cartPrice),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(R.string.misc_price_separator),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
        }
        Text(
            text = currencyFormatter.format(totalPrice),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
        )
    }
}

@Composable
fun ProductList(onProductDeletion: (String) -> Unit,
                onToggleProdAddedToCart: (String) -> Unit,
                onProductIncQty: (String, Int) -> Unit,
                groceryList: List<ProductEntry>,
                appInfo: AppInfo,
                modifier: Modifier = Modifier){
    Column(modifier = modifier.padding(dimensionResource(R.dimen.padding_small))){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_small)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }
        LazyColumn(modifier = if (appInfo == AppInfo.AWAITING_INPUT) Modifier.fillMaxHeight() else Modifier) {
            items(groceryList, key = { it.id }) { productEntry ->
                ProductEntryCard(
                    productEntry = productEntry,
                    onProductDeletion = onProductDeletion,
                    onToggleProdAddedToCart = onToggleProdAddedToCart,
                    onProductIncQty = onProductIncQty,
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 250),
                        fadeOutSpec = tween(durationMillis = 250),
                        placementSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy)
                    ),
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProductListPreview(){
    CompraListaTheme {
        Surface {
            ProductList(
                onProductDeletion = {},
                onProductIncQty = {_, _ -> },
                onToggleProdAddedToCart = {},
                appInfo = AppInfo.LOADING,
                groceryList = listOf(
                    ProductEntry(
                        id = "1",
                        name = "Mantequilla de Maní",
                        unitPrice = 17000.0,
                        addedToCart = true,
                        quantity = 2,
                        superMarketListId = 0
                    ),
                    ProductEntry(
                        id = "2",
                        name = "Doritos de queso",
                        unitPrice = 3000.0,
                        quantity = 1,
                        superMarketListId = 0
                    ),
                    ProductEntry(
                        id = "3",
                        name = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                        unitPrice = 8000.0,
                        quantity = 3,
                        superMarketListId = 0
                    ),
                    ProductEntry(
                        id = "4",
                        name = "Optimus Prime",
                        addedToCart = true,
                        unitPrice = 30000.0,
                        quantity = 1,
                        superMarketListId = 0
                    ),
                )
            )
        }
    }
}


