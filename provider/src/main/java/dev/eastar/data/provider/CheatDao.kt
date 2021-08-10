package dev.eastar.data.provider

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CheatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(goods: CheatEntity): Long

    @Query("SELECT * FROM CHEAT")
    fun getItems(): Cursor

    @Query("SELECT * FROM CHEAT WHERE key = :key")
    fun getItem(key: String): Cursor

    @Query("SELECT * FROM CHEAT WHERE id = :id")
    fun getItem(id: Long): Cursor
}