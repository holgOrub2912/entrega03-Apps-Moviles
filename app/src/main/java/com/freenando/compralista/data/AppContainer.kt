package com.freenando.compralista.data

import android.content.Context

interface AppContainer {
    val entriesRepository: EntriesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val entriesRepository: EntriesRepository by lazy {
        OfflineEntriesRepository(EntryDatabase.getDatabase(context).entryDao())
    }
}