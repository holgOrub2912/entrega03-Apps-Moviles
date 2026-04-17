package com.freenando.compralista.data

import kotlinx.coroutines.flow.Flow

class OfflineEntriesRepository(private val entryDao: EntryDao, private val superMarketListDao: SuperMarketListDao) : EntriesRepository {
    override fun getSupermarketListStream(): Flow<List<SupermarketList>> = superMarketListDao.getAllSupermarketLists()

    override fun getSupermarketList(id: Int): Flow<SupermarketList?> = superMarketListDao.getSupermarketList(id)

    override suspend fun insertSupermarketList(supermarketList: SupermarketList) = superMarketListDao.insert(supermarketList)

    override suspend fun deleteSupermarketList(supermarketList: SupermarketList) = superMarketListDao.delete(supermarketList)

    override suspend fun updateSupermarketList(supermarketList: SupermarketList) = superMarketListDao.update(supermarketList)

    override fun getEntriesInSupermarketListStream(superMarketListId: Int): Flow<List<ProductEntry>> = entryDao.getEntriesInSuperMarketList(superMarketListId)

    override fun getEntryStream(id: String): Flow<ProductEntry?> = entryDao.getEntry(id)

    override suspend fun insertEntry(entry: ProductEntry) = entryDao.insert(entry)

    override suspend fun deleteEntry(entry: ProductEntry) = entryDao.delete(entry)

    override suspend fun updateEntry(entry: ProductEntry) = entryDao.update(entry)

}