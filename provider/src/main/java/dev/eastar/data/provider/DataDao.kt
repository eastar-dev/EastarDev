package dev.eastar.data.provider

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(vararg goods: DataEntity): LongArray

    @Query("SELECT * FROM DATA")
    fun getItem(): Cursor
}