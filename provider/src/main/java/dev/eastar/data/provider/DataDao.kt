package dev.eastar.data.provider

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDatas(vararg goods: DataEntity): LongArray

    @Query("SELECT * FROM DATA ORDER BY ROWID DESC LIMIT 1")
    suspend fun getData(): DataEntity

    @Query("SELECT * FROM DATA WHERE key = :key")
    suspend fun getDatas(key: String): List<DataEntity>

}