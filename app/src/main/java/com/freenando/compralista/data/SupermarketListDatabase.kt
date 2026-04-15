package com.freenando.compralista.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SupermarketList::class], version = 3, exportSchema = false)
abstract class SupermarketListDatabase : RoomDatabase() {
    abstract fun supermarketListDao(): SuperMarketListDao

    companion object {
        @Volatile
        private var Instance: SupermarketListDatabase? = null;

        fun getDatabase(context: Context): SupermarketListDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, SupermarketListDatabase::class.java, "supermarket_list_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
