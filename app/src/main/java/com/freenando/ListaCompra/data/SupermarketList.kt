package com.freenando.ListaCompra.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.freenando.ListaCompra.search.AllSupermarketSearcher

@Entity(tableName = "supermarketLists")
data class SupermarketList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val searcher: AllSupermarketSearcher,
){
    constructor(info: SupermarketListInfo): this(
        info.id,
        info.name,
        info.searcher
    )
}

data class SupermarketListInfo(
    val id: Int = 0,
    val name: String,
    val searcher: AllSupermarketSearcher,
    val totalPrice: Double,
    val totalProducts: Int
)