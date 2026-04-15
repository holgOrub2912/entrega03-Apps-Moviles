package com.freenando.compralista.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: ProductEntry)

    @Update
    suspend fun update(entry: ProductEntry)

    @Delete
    suspend fun delete(entry: ProductEntry)

    @Query("SELECT * from productEntries WHERE id = :id")
    fun getEntry(id: String): Flow<ProductEntry>

    @Query("SELECT * from productEntries WHERE superMarketListId = :superMarketListId")
    fun getEntriesInSuperMarketList(superMarketListId: Int): Flow<List<ProductEntry>>
}