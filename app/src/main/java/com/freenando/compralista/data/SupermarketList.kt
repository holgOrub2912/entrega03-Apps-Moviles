package com.freenando.compralista.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.freenando.compralista.search.AllSupermarketSearcher

@Entity(tableName = "supermarketLists")
data class SupermarketList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val searcher: AllSupermarketSearcher,
)