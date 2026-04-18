package com.freenando.ListaCompra.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freenando.ListaCompra.R
import com.freenando.ListaCompra.data.EntriesRepository
import com.freenando.ListaCompra.data.SupermarketList
import com.freenando.ListaCompra.search.AllSupermarketSearcher
import com.freenando.ListaCompra.ui.theme.CompraListaTheme

@Composable
fun AddNewListScreen(
    viewModel: SupermarketListViewModel,
    onSupermarketListAdded: () -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
){
    NewListForm({name, searcher ->
        viewModel.addList(
            SupermarketList(
                name = name,
                searcher = searcher,
            )
        )
        onSupermarketListAdded()
    },
        onNavBack = onNavBack
    )
}

@Composable
fun NewListForm(
    onAddList: (String, AllSupermarketSearcher) -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier
){
    var listName by remember { mutableStateOf("") }
    var listSearcher: AllSupermarketSearcher? by remember { mutableStateOf(null) }
    var menuExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.padding_small))
            ) {
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
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                TextField(
                    value = listName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium),
                    onValueChange = { listName = it },
                    label = { Text(stringResource(R.string.label_supermarket_list_name_textfield)) }
                )
                Column {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        colors = ButtonColors(
                            containerColor = if (listSearcher == null ) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.secondary,
                            contentColor = if (listSearcher == null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.onError,
                            disabledContentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {menuExpanded = !menuExpanded},
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        if (listSearcher != null)
                            Text(listSearcher!!.getSupermarketName())
                        else
                            Text(stringResource(R.string.no_supermarket_selected))
                        Icon(
                            Icons.Rounded.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.display_supermarket_options),
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        modifier = Modifier.fillMaxWidth(),
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        AllSupermarketSearcher.entries.map {searcher ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = {Text(searcher.getSupermarketName())},
                                onClick = {
                                    listSearcher = searcher
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
                Button(
                    enabled = listName.isNotEmpty() && listSearcher != null,
                    modifier = Modifier
                        .padding(top = 30.dp),
                    onClick = { onAddList(listName, listSearcher!!) },
                    shape = MaterialTheme.shapes.small,
                ){
                    Text(
                        stringResource(R.string.add_supermarket_list_btn),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(
                                vertical = 4.dp,
                                horizontal = 8.dp
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.4f))
        }

}

@Composable
@Preview
fun AddNewListScreenPreview(){
    CompraListaTheme {
        Surface {
            NewListForm(
                onAddList = {_, _ -> },
                onNavBack = {}
            )
        }
    }
}