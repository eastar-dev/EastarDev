package dev.eastar.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room



class DataProvider : ContentProvider() {

    val PACKAGE_NAME = "dev.eastar.dev"
    val AUTHORITY = "$PACKAGE_NAME.data_provider"

    enum class TYPES(val vendor: String, val path: String) {
        DATA_DIR("vnd.dev.eastar.cursor.dir/vnd.dev.eastar.cursor.data_provider.data", "data"),
        DATA_ITEM("vnd.dev.eastar.cursor.item/vnd.dev.eastar.cursor.data_provider.data", "data/#")
    }

    private val Int.types: TYPES
        get() = TYPES.values()[this]

    private val ContentValues?.toDataEntity: DataEntity
        get() =  null


    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TYPES.DATA_DIR.path, TYPES.DATA_DIR.ordinal)
        addURI(AUTHORITY, TYPES.DATA_ITEM.path, TYPES.DATA_ITEM.ordinal)
    }

    override fun getType(uri: Uri): String? {
        val ordinal = sUriMatcher.match(uri)
        if (ordinal == UriMatcher.NO_MATCH) return null
        return TYPES.values()[ordinal].vendor
    }

    private lateinit var appDatabase: DataDatabase
    private lateinit var dataDao: DataDao

    override fun onCreate(): Boolean {
        appDatabase = Room.databaseBuilder(context!!, DataDatabase::class.java, "DATA").build()
        dataDao = appDatabase.dataDao()
        return true
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0


    override fun insert(uri: Uri, values: ContentValues?): Uri? = when (sUriMatcher.match(uri).types) {
        TYPES.DATA_DIR -> null
        TYPES.DATA_ITEM -> dataDao.insertDatas(values.toDataEntity)
        else -> null
    }


    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = when (sUriMatcher.match(uri).types) {
        TYPES.DATA_DIR -> null
        TYPES.DATA_ITEM -> null
        else -> null
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}