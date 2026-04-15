package com.freenando.compralista.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.freenando.compralista.search.AllSupermarketSearcher

@Entity(tableName = "supermarketLists")
data class SupermarketList(
    @PrimaryKey
    val id: Int,
    val name: String,
    val searcher: AllSupermarketSearcher,
)