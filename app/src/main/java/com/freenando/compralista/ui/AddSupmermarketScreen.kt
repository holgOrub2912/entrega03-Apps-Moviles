package com.freenando.compralista.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.freenando.compralista.R

@Composable
fun AddNewListScreen(
    modifier: Modifier = Modifier
){
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ){
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Hi") }
        )
    }
}