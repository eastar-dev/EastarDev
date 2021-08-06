package dev.eastar.data.provider

import androidx.annotation.Keep
import androidx.room.Entity

@Keep
@Entity(
    tableName = "DATA",
    primaryKeys = ["id"]
)
data class DataEntity(
    var id: Long,
    var key: String = "",
    var value: String = "",
)
