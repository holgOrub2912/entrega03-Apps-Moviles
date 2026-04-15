package com.freenando.compralista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.freenando.compralista.data.AppContainer
import com.freenando.compralista.data.AppDataContainer
import com.freenando.compralista.data.ProductEntry
import com.freenando.compralista.data.SupermarketList
import com.freenando.compralista.search.AllSupermarketSearcher
import com.freenando.compralista.ui.AddNewListScreen
import com.freenando.compralista.ui.AppInfo
import com.freenando.compralista.ui.GroceryListViewModel
import com.freenando.compralista.ui.GroceryListViewModelProvider
import com.freenando.compralista.ui.ProductListScreen
import com.freenando.compralista.ui.theme.CompraListaTheme
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    private lateinit var groceryListViewModel: GroceryListViewModel
    val container: AppContainer = AppDataContainer(this)
    
    private val currencyFormatter = NumberFormat.getCurrencyInstance()

    
    private val barcodeLauncher =
        registerForActivityResult( ScanContract() ){ result ->
            if (result.contents != null)
                groceryListViewModel.addProduct(result.contents)
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

    private fun scanBarcode(){
        barcodeLauncher.launch(ScanOptions()
                .setOrientationLocked(true)
                .setPrompt(resources.getString(R.string.scan_prompt)))
    }


    @Composable
    fun CompraListaApp(
        context: MainActivity,
        navController: NavHostController = rememberNavController(),
    ){
        val layoutDirection = LocalLayoutDirection.current;
        val supermarketList = remember { SupermarketList(
            id = 0,
            name = "Mis cositas del éxito",
            searcher = AllSupermarketSearcher.EXITO
        ) }
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
                startDestination = OverviewListScreen.ProductList.name,
                modifier = Modifier
                    .fillMaxSize()
            ){
                composable(route = OverviewListScreen.Start.name) {
                    OverviewListScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable (route = OverviewListScreen.NewList.name){
                    AddNewListScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                composable (route = OverviewListScreen.ProductList.name){
                    ProductListScreen(
                        supermarketList = supermarketList,
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
