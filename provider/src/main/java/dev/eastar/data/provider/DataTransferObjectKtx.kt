package dev.eastar.data.provider

import android.content.ContentValues

object DataTransferObjectKtx {
    val ContentValues.toCheatEntity: CheatEntity
        get() = CheatEntity(key = getAsString("key"), value = getAsString("value"))
}