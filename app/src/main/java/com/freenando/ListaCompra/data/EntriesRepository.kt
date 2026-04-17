package com.freenando.ListaCompra.data

import kotlinx.coroutines.flow.Flow

interface EntriesRepository {
    fun getSupermarketListStream() : Flow<List<SupermarketListInfo>>

    fun getSupermarketList(id: Int): Flow<SupermarketList?>

    suspend fun insertSupermarketList(supermarketList: SupermarketList)

    suspend fun deleteSupermarketList(supermarketList: SupermarketList)

    suspend fun updateSupermarketList(supermarketList: SupermarketList)
    fun getEntriesInSupermarketListStream(superMarketListId: Int): Flow<List<ProductEntry>>

    fun getEntryStream(id: String): Flow<ProductEntry?>

    suspend fun insertEntry(entry: ProductEntry)

    suspend fun deleteEntry(entry: ProductEntry)

    suspend fun updateEntry(entry: ProductEntry)
}