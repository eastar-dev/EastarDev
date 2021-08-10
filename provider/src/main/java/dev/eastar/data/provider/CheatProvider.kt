package dev.eastar.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.log.Log
import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import dev.eastar.data.provider.DataTransferObjectKtx.toCheatEntity


class CheatProvider : ContentProvider() {
    override fun getType(uri: Uri): String? {
        val ordinal = sUriMatcher.match(uri)
        if (ordinal == UriMatcher.NO_MATCH) return null
        return TYPES.values()[ordinal].vendor
    }

    private lateinit var cheatDatabase: CheatDatabase
    private lateinit var cheatDao: CheatDao

    override fun onCreate(): Boolean {
        cheatDatabase = Room.inMemoryDatabaseBuilder(context!!, CheatDatabase::class.java).build()
        //cheatDatabase = Room.databaseBuilder(context!!, CheatDatabase::class.java, "DATA").fallbackToDestructiveMigration().build()

        cheatDao = cheatDatabase.dao()
        return true
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values ?: return null
        return when (sUriMatcher.match(uri).types) {
            TYPES.DATA_DIR -> {
                val key = cheatDao.insertItem(values.toCheatEntity)
                val uriWithId = Uri.withAppendedPath(uri, key.toString())
                context?.contentResolver?.notifyChange(uriWithId, null)
                uriWithId
            }
            else -> null
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = when (uri.match) {
        TYPES.DATA_DIR -> {
            val cursor = cheatDao.getItems()
            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        TYPES.DATA_ITEM_KEY -> {
            val key = uri.lastPathSegment!!
            val cursor = cheatDao.getItem(key)
            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        TYPES.DATA_ITEM -> {
            val id = uri.lastPathSegment!!.toLong()
            val cursor = cheatDao.getItem(id)
            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        else -> null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    enum class TYPES(val vendor: String, val path: String) {
        DATA_DIR("vnd.dev.eastar.cursor.dir/vnd.dev.eastar.cursor.cheat.provider.data", "data"),
        DATA_ITEM("vnd.dev.eastar.cursor.item/vnd.dev.eastar.cursor.cheat.provider.data", "data/#"),
        DATA_ITEM_KEY("vnd.dev.eastar.cursor.item/vnd.dev.eastar.cursor.cheat.provider.data", "data/*")
    }

    companion object {
        private const val PACKAGE_NAME = "dev.eastar.dev"
        const val AUTHORITY = "${PACKAGE_NAME}.cheat.provider"

        @VisibleForTesting
        public fun getUriMatcher() = sUriMatcher

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TYPES.DATA_DIR.path, TYPES.DATA_DIR.ordinal)
            addURI(AUTHORITY, TYPES.DATA_ITEM.path, TYPES.DATA_ITEM.ordinal)
        }

        private val Int.types: TYPES?
            get() = if (this == UriMatcher.NO_MATCH) null else TYPES.values()[this]

        private val Uri.match: TYPES?
            get() {
                val result = sUriMatcher.match(this).types
                Log.e(this)
                Log.e(result)
                return result
            }
    }
}