package com.freenando.compralista.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.freenando.compralista.R
import com.freenando.compralista.data.EntriesRepository
import com.freenando.compralista.data.SupermarketList
import com.freenando.compralista.search.AllSupermarketSearcher
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.coroutineContext

@Composable
fun AddNewListScreen(
    viewModel: SupermarketListViewModel,
    onSupermarketListAdded: () -> Unit,
    modifier: Modifier = Modifier
){
    var listName by remember { mutableStateOf("") }
    var listSearcher: AllSupermarketSearcher? by remember { mutableStateOf(null) }
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        TextField(
            value = listName,
            onValueChange = { listName = it },
            label = { Text(stringResource(R.string.label_supermarket_list_name_textfield)) }
        )
        Button(onClick = {menuExpanded = !menuExpanded}) {
            if (listSearcher != null)
                Text(listSearcher!!.getSupermarketName())
            else
                Text("No searcher")
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            AllSupermarketSearcher.entries.map {searcher ->
                DropdownMenuItem(
                    text = {Text(searcher.getSupermarketName())},
                    onClick = {
                        listSearcher = searcher
                        menuExpanded = false
                    }
                )
            }
        }
        Button(
            enabled = listName.isNotEmpty() && listSearcher != null,
            modifier = Modifier,
            onClick = {
                viewModel.addList(
                    SupermarketList(
                        name = listName,
                        searcher = listSearcher!!,
                    )
                )
                onSupermarketListAdded()
            },
        ){
            Text("Add list")
        }
    }
}