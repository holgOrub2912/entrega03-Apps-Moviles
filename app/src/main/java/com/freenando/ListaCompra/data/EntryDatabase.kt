package com.freenando.ListaCompra.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntry::class, SupermarketList::class], version = 7, exportSchema = false)
abstract class EntryDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
    abstract fun supermarketListDao(): SuperMarketListDao

    companion object {
        @Volatile
        private var Instance: EntryDatabase? = null;

        fun getDatabase(context: Context): EntryDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, EntryDatabase::class.java, "entry_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}