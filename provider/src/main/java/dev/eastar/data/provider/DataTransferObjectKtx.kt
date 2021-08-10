package dev.eastar.data.provider

import android.content.ContentValues

object DataTransferObjectKtx {
    val ContentValues?.toDataEntity: DataEntity
        get() = DataEntity(0)
}