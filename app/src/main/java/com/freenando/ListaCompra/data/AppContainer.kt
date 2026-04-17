package com.freenando.ListaCompra.data

import android.content.Context

interface AppContainer {
    val entriesRepository: EntriesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val entriesRepository: EntriesRepository by lazy {
        val database = EntryDatabase.getDatabase(context)
        OfflineEntriesRepository(
            database.entryDao(),
            database.supermarketListDao()
        )
    }
}