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

    @Query("SELECT * FROM supermarketLists")
    fun getAllSupermarketLists(): Flow<List<SupermarketList>>

    @Query("SELECT * FROM supermarketLists WHERE id = :id")
    fun getSupermarketList(id: Int): Flow<SupermarketList?>
}