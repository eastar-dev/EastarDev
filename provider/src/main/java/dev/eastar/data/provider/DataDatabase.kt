package dev.eastar.data.provider

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DataEntity::class],
    version = 1
)
abstract class DataDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao
}

