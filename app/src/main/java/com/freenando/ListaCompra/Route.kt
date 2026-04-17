package com.freenando.ListaCompra

import androidx.annotation.StringRes

sealed class AppScreen(val route: String, @StringRes val title: Int){
    data object Start: AppScreen("home", R.string.app_name)
    data object NewList: AppScreen("addlist", R.string.add_supermarket_list)
    data object ComparePrices: AppScreen("compare/{ean}", R.string.compare_price) {
        fun createRoute(ean: String): String = "compare/$ean"
    }
    data object ProductList: AppScreen("list/{supermarketListId}?newProductEan={ean}", R.string.supermarket_list){
        fun createRoute(supermarketListId: Int): String = "list/$supermarketListId"
        fun createRoute(supermarketListId: Int, newProductEan: String): String = "list/$supermarketListId?newProductEan=$newProductEan"
    }
}