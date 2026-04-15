package com.freenando.compralista

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.freenando.compralista.R

enum class OverviewListScreen(@StringRes val title: Int){
    Start(title = R.string.app_name),
    NewList(title = R.string.add_supermarket_list),
    ComparePrices(title = R.string.compare_price),
    ProductList(title = R.string.supermarket_list)
}

@Composable
fun OverviewListScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier
    ) {
        Text("content here")
    }

}