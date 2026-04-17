package com.freenando.compralista

import android.net.wifi.ScanResult
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.freenando.compralista.data.AppContainer
import com.freenando.compralista.data.AppDataContainer
import com.freenando.compralista.data.SupermarketList
import com.freenando.compralista.search.AllSupermarketSearcher
import com.freenando.compralista.ui.AddNewListScreen
import com.freenando.compralista.ui.GroceryListViewModel
import com.freenando.compralista.ui.ProductListScreen
import com.freenando.compralista.ui.SupermarketListViewModel
import com.freenando.compralista.ui.theme.CompraListaTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    private lateinit var groceryListViewModel: GroceryListViewModel
    val container: AppContainer = AppDataContainer(this)
    
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
    lateinit var scanProductAction: (String) -> Unit

    private val barcodeLauncher =
        registerForActivityResult( ScanContract() ){ result ->
            if (result.contents != null)
                scanProductAction(result.contents)
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


    @Composable
    fun CompraListaApp(
        context: MainActivity,
    ){
        val layoutDirection = LocalLayoutDirection.current;
        val supermarketListViewModel = SupermarketListViewModel(repository = context.container.entriesRepository)
        val navController: NavHostController = rememberNavController()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
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
                        }
                    )
                }
                composable(AppScreen.NewList.route) {
                    AddNewListScreen(
                        viewModel = supermarketListViewModel,
                        onSupermarketListAdded = {navController.popBackStack()},
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable(AppScreen.ProductList.route, arguments = listOf(
                    navArgument("supermarketListId"){
                        type = NavType.IntType
                    })){backStackEntry ->
                    val supermarketListId = backStackEntry.arguments?.getInt("supermarketListId") ?: return@composable
                    ProductListScreen(
                        supermarketListId = supermarketListId,
                        context = context,
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
