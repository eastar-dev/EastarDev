package dev.eastar.data.provider

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CheatEntity::class],
    version = 2
)
abstract class CheatDatabase : RoomDatabase() {
    abstract fun dao(): CheatDao
}

