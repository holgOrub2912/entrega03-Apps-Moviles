package com.freenando.compralista.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SuperMarketListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(supermarketList: SupermarketList)

    @Update
    suspend fun update(supermarketList: SupermarketList)

    @Delete
    suspend fun delete(supermarketList: SupermarketList)

    @Query("SELECT supermarketLists.id id, supermarketLists.name name, supermarketLists.searcher searcher, SUM(productEntries.quantity * productEntries.unitPrice) totalPrice FROM supermarketLists LEFT JOIN productEntries ON supermarketLists.id = productEntries.superMarketListId GROUP BY supermarketLIsts.id")
    fun getAllSupermarketLists(): Flow<List<SupermarketListInfo>>

    @Query("SELECT * FROM supermarketLists WHERE id = :id")
    fun getSupermarketList(id: Int): Flow<SupermarketList?>
}