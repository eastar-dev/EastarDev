package dev.eastar.data.provider

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "CHEAT")
data class CheatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val key: String = "",
    val value: String = "",
)
