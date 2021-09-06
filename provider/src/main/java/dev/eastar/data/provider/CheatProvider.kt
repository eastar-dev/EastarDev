package dev.eastar.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.log.Log
import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import dev.eastar.data.di.CheatDatabaseModule


class CheatProvider : ContentProvider() {
    override fun getType(uri: Uri): String? {
        val ordinal = sUriMatcher.match(uri)
        if (ordinal == UriMatcher.NO_MATCH) return null
        return TYPES.values()[ordinal].vendor
    }

    private lateinit var cheatDatabase: CheatDatabase

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface CheatProviderEntryPoint {
        fun cheatDatabase(): CheatDatabase
    }

    override fun onCreate(): Boolean {
        // cheatDatabase = Room.inMemoryDatabaseBuilder(context!!, CheatDatabase::class.java).build()
        //cheatDatabase = Room.databaseBuilder(context!!, CheatDatabase::class.java, "DATA").fallbackToDestructiveMigration().build()

        val appContext = context?.applicationContext ?: throw IllegalStateException()
        val hiltEntryPoint = EntryPointAccessors.fromApplication(appContext, CheatProviderEntryPoint::class.java)
        cheatDatabase = hiltEntryPoint.cheatDatabase()

        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        values ?: return null



        return when (sUriMatcher.match(uri).types) {
            TYPES.DATA_DIR -> {
                val db = cheatDatabase.openHelper.writableDatabase
                val retId: Long = db.insert(TYPES.DATA_DIR.table, SQLiteDatabase.CONFLICT_REPLACE, values)

                return ContentUris.withAppendedId(uri, retId)
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
            val cursor = cheatDatabase.openHelper.readableDatabase.query(SupportSQLiteQueryBuilder.builder(TYPES.DATA_DIR.table)
                .selection(selection, selectionArgs)
                .columns(projection)
                .orderBy(sortOrder)
                .create())

            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        TYPES.DATA_ITEM_KEY -> {
            val key = uri.lastPathSegment!!
            val cursor = cheatDatabase.query(SupportSQLiteQueryBuilder.builder(TYPES.DATA_DIR.table)
                .selection("key=?", arrayOf(key))
                .columns(projection)
                .orderBy(sortOrder)
                .create())

            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        TYPES.DATA_ITEM -> {
            val id = uri.lastPathSegment!!
            val cursor = cheatDatabase.query(SupportSQLiteQueryBuilder.builder(TYPES.DATA_DIR.table)
                .selection("id=?", arrayOf(id))
                .columns(projection)
                .orderBy(sortOrder)
                .create())

            context?.let { cursor.setNotificationUri(it.contentResolver, uri) }
            cursor
        }
        else -> null
    }


    override fun update(uri: Uri,
                        values: ContentValues?,
                        selection: String?,
                        selectionArgs: Array<String>?
    ): Int = when (uri.match) {
        TYPES.DATA_DIR -> cheatDatabase.openHelper.writableDatabase.update(TYPES.DATA_DIR.table,
            SQLiteDatabase.CONFLICT_REPLACE,
            values,
            selection, selectionArgs)
        TYPES.DATA_ITEM_KEY -> {
            val key = uri.lastPathSegment!!
            cheatDatabase.openHelper.writableDatabase.update(TYPES.DATA_DIR.table,
                SQLiteDatabase.CONFLICT_REPLACE,
                values,
                "key=?", arrayOf(key))
        }
        TYPES.DATA_ITEM -> {
            val id = uri.lastPathSegment!!
            cheatDatabase.openHelper.writableDatabase.update(TYPES.DATA_DIR.table,
                SQLiteDatabase.CONFLICT_REPLACE,
                values,
                "id=?", arrayOf(id))
        }
        else -> 0
    }

    override fun delete(uri: Uri,
                        selection: String?,
                        selectionArgs: Array<String>?
    ): Int = when (uri.match) {
        TYPES.DATA_DIR -> cheatDatabase.openHelper.writableDatabase.delete(TYPES.DATA_DIR.table,
            selection,
            selectionArgs)

        TYPES.DATA_ITEM_KEY -> {
            val key = uri.lastPathSegment!!
            cheatDatabase.openHelper.writableDatabase.delete(TYPES.DATA_DIR.table,
                "key=?", arrayOf(key))
        }
        TYPES.DATA_ITEM -> {
            val id = uri.lastPathSegment!!
            cheatDatabase.openHelper.writableDatabase.delete(TYPES.DATA_DIR.table,
                "id=?", arrayOf(id))
        }
        else -> 0
    }

    enum class TYPES(val vendor: String, val table: String) {
        DATA_DIR("vnd.dev.eastar.cursor.dir/vnd.dev.eastar.cursor.cheat.provider.data", "CHEAT"),
        DATA_ITEM("vnd.dev.eastar.cursor.item/vnd.dev.eastar.cursor.cheat.provider.data", "CHEAT/#"),
        DATA_ITEM_KEY("vnd.dev.eastar.cursor.item/vnd.dev.eastar.cursor.cheat.provider.data", "CHEAT/*")
    }

    companion object {
        private const val PACKAGE_NAME = "dev.eastar.dev"
        const val AUTHORITY = "${PACKAGE_NAME}.cheat.provider"


        @VisibleForTesting
        public fun getUriMatcher() = sUriMatcher

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TYPES.DATA_DIR.table, TYPES.DATA_DIR.ordinal)
            addURI(AUTHORITY, TYPES.DATA_ITEM.table, TYPES.DATA_ITEM.ordinal)
            addURI(AUTHORITY, TYPES.DATA_ITEM_KEY.table, TYPES.DATA_ITEM_KEY.ordinal)
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

        val URI = Uri.parse("content://$AUTHORITY/${TYPES.DATA_DIR.table}")
    }
}