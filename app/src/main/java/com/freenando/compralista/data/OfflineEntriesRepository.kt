package com.freenando.compralista.data

import kotlinx.coroutines.flow.Flow

class OfflineEntriesRepository(private val entryDao: EntryDao) : EntriesRepository {
    override fun getAllEntriesStream(): Flow<List<ProductEntry>> = entryDao.getAllEntries()

    override fun getEntryStream(id: String): Flow<ProductEntry?> = entryDao.getEntry(id)

    override suspend fun insertEntry(entry: ProductEntry) = entryDao.insert(entry)

    override suspend fun deleteEntry(entry: ProductEntry) = entryDao.delete(entry)

    override suspend fun updateEntry(entry: ProductEntry) = entryDao.update(entry)

}