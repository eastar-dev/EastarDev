package dev.eastar.data.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.log.Log
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dev.eastar.data.provider.CheatProvider.TYPES.DATA_DIR
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DataProviderTest {
    private lateinit var apiDao: CheatDao
    private lateinit var db: CheatDatabase

    private lateinit var resolver: ContentResolver

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CheatDatabase::class.java).build()
        apiDao = db.dao()
    }

    @Before
    fun createResolver() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertThat(appContext.packageName).isEqualTo("dev.eastar.dev")
        resolver = appContext.contentResolver
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() {
        val uri = CheatProvider.URI
        val millis = System.currentTimeMillis()
        val uriId = resolver.insert(uri, ContentValues().apply {
            put("key", "key$millis")
            put("value", "$millis")
        })
        Log.e(uriId)
        assertThat(uriId).isNotNull()
        val cursor = resolver.query(uriId!!, null, null, null, null)
        assertThat(cursor).isNotNull()
        assertThat(cursor?.count).isEqualTo(1)
        assertThat(cursor?.moveToFirst()).isTrue()
        assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("key"))).isEqualTo("key$millis")
        assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("value"))).isEqualTo("$millis")
    }

    @Test
    fun getTypeItem() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = CheatProvider.URI

        //when
        val actual = matcher.match(uri)

        //then
        assertThat(actual).isEqualTo(DATA_DIR.ordinal)
    }

    @Test
    fun getTypeItem2() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = Uri.withAppendedPath(CheatProvider.URI, "" + 100)

        //when
        val actual = matcher.match(uri)

        //then
        assertThat(actual).isEqualTo(CheatProvider.TYPES.DATA_ITEM.ordinal)
    }

    @Test
    fun getTypeItem3() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = Uri.withAppendedPath(CheatProvider.URI, "key1")

        //when
        val actual = matcher.match(uri)

        //then
        assertThat(actual).isEqualTo(CheatProvider.TYPES.DATA_ITEM_KEY.ordinal)
    }

    @Test
    fun getTypeDir() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = CheatProvider.URI

        //when
        val actual = matcher.match(uri)

        //then
        assertThat(actual).isEqualTo(0)
    }
}