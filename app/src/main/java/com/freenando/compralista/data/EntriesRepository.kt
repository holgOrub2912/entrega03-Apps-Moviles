package com.freenando.compralista.data

import kotlinx.coroutines.flow.Flow

interface EntriesRepository {
    fun getAllEntriesStream(): Flow<List<ProductEntry>>

    fun getEntryStream(id: String): Flow<ProductEntry?>

    suspend fun insertEntry(entry: ProductEntry)

    suspend fun deleteEntry(entry: ProductEntry)

    suspend fun updateEntry(entry: ProductEntry)
}