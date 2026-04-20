package com.freenando.ListaCompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.freenando.ListaCompra.data.AppContainer
import com.freenando.ListaCompra.data.AppDataContainer
import com.freenando.ListaCompra.ui.AddNewListScreen
import com.freenando.ListaCompra.ui.ComparePriceScreen
import com.freenando.ListaCompra.ui.GroceryListViewModel
import com.freenando.ListaCompra.ui.OverviewListScreen
import com.freenando.ListaCompra.ui.ProductListScreen
import com.freenando.ListaCompra.ui.SupermarketListViewModel
import com.freenando.ListaCompra.ui.theme.CompraListaTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    private lateinit var groceryListViewModel: GroceryListViewModel
    val container: AppContainer = AppDataContainer(this)
    
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
    var scanProductAction: (String) -> Unit = {}
    private lateinit var compareAction: (String) -> Unit

    private val barcodeLauncher =
        registerForActivityResult( ScanContract() ){ result ->
            if (result.contents != null)
                scanProductAction(result.contents)
        }

    private val compareBarcodeLauncher =
        registerForActivityResult( ScanContract() ){ result ->
            if (result.contents != null)
                compareAction(result.contents)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompraListaTheme {
                CompraListaApp(context = this)
            }
        }
    }

    fun scanBarcode(){
        barcodeLauncher.launch(ScanOptions()
                .setOrientationLocked(true)
                .setPrompt(resources.getString(R.string.scan_prompt)))
    }

    fun compareScanBarcode(){
        compareBarcodeLauncher.launch(ScanOptions()
            .setOrientationLocked(true)
            .setPrompt(resources.getString(R.string.scan_prompt)))

    }


    @Composable
    fun CompraListaApp(
        context: MainActivity,
        supermarketListViewModel: SupermarketListViewModel = SupermarketListViewModel(repository = context.container.entriesRepository)
    ){
        val layoutDirection = LocalLayoutDirection.current;
        val supermarketLists by supermarketListViewModel.uiState.collectAsState()
        val navController: NavHostController = rememberNavController()
        context.compareAction = {ean ->
            navController.navigate(AppScreen.ComparePrices.createRoute(ean))
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(
                    start = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateStartPadding(layoutDirection),
                    end = WindowInsets.safeDrawing.asPaddingValues()
                        .calculateEndPadding(layoutDirection),
                ),
        ){
            NavHost(
                navController = navController,
                startDestination = AppScreen.Start.route,
                modifier = Modifier
                    .fillMaxSize()
            ){
                composable(AppScreen.Start.route) {
                    OverviewListScreen(
                        repository = context.container.entriesRepository,
                        onNavigateToNewList = {
                            navController.navigate(route = AppScreen.NewList.route)
                        },
                        onNavigateToExistingList = {
                            navController.navigate(AppScreen.ProductList.createRoute(it))
                        },
                        onNavigateToCompare = { context.compareScanBarcode() },
                        onDeleteList = { supermarketListViewModel.deleteList(it) }
                    )
                }
                composable(AppScreen.NewList.route) {
                    AddNewListScreen(
                        viewModel = supermarketListViewModel,
                        onSupermarketListAdded = {navController.popBackStack()},
                        onNavBack = {navController.popBackStack()},
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(AppScreen.ProductList.route, arguments = listOf(
                    navArgument("supermarketListId"){
                        type = NavType.IntType
                    },
                    navArgument("ean"){
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })){backStackEntry ->
                    val supermarketListId = backStackEntry.arguments?.getInt("supermarketListId") ?: return@composable
                    val newProductEan = backStackEntry.arguments?.getString("ean")
                    ProductListScreen(
                        supermarketListId = supermarketListId,
                        context = context,
                        newProductEan = newProductEan,
                        onNavBack = {navController.popBackStack()},
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(AppScreen.ComparePrices.route, arguments = listOf(
                    navArgument("ean"){
                        type = NavType.StringType
                    })){backStackEntry ->
                    val ean = backStackEntry.arguments?.getString("ean") ?: return@composable
                    ComparePriceScreen(
                        ean = ean,
                        searchers = supermarketLists.list,
                        onAddToList = {ean, listId ->
                            navController.popBackStack()
                            navController.navigate(AppScreen.ProductList.createRoute(listId, ean))
                      },
                        onNavBack = {navController.popBackStack()},
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
