package dev.eastar.data.provider

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CheatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(goods: CheatEntity): Long

    @Query("SELECT * FROM CHEAT")
    suspend fun getItems(): List<CheatEntity>

    @Query("SELECT * FROM CHEAT WHERE 'key' = :key")
    suspend fun getItem(key: String): CheatEntity

    @Query("SELECT * FROM CHEAT WHERE id = :id")
    suspend fun getItem(id: Long): CheatEntity
}