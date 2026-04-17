package com.freenando.compralista.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.freenando.compralista.search.AllSupermarketSearcher

@Entity(tableName = "supermarketLists")
data class SupermarketList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val searcher: AllSupermarketSearcher,
)

data class SupermarketListInfo(
    val id: Int = 0,
    val name: String,
    val searcher: AllSupermarketSearcher,
    val totalPrice: Double
)