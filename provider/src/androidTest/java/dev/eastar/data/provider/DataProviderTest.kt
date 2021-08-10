package dev.eastar.data.provider

import android.content.ContentValues
import android.content.Context
import android.log.Log
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DataProviderTest {
    private lateinit var apiDao: CheatDao
    private lateinit var db: CheatDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CheatDatabase::class.java).build()
        apiDao = db.dao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dev.eastar.dev", appContext.packageName)

        val uri = Uri.parse("content://${CheatProvider.AUTHORITY}/${CheatProvider.TYPES.DATA_DIR.path}")
        val millis = System.currentTimeMillis()
        val uriId = appContext.contentResolver.insert(uri, ContentValues().apply {
            put("key", "key$millis")
            put("value", "$millis")
        })
        Log.e(uriId)
        Truth.assertThat(uriId).isNotNull()
        val cursor = appContext.contentResolver.query(uriId!!, null, null, null, null)
        Truth.assertThat(cursor).isNotNull()
        Truth.assertThat(cursor?.count).isEqualTo(1)
        Truth.assertThat(cursor?.moveToFirst()).isTrue()
        Truth.assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("key"))).isEqualTo("key$millis")
        Truth.assertThat(cursor?.getString(cursor.getColumnIndexOrThrow("value"))).isEqualTo("$millis")
    }

    @Test
    fun query() {
    }

    @Test
    fun getTypeItem() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = Uri.parse("content://${CheatProvider.AUTHORITY}/data/id")

        //when
        val actual = matcher.match(uri)

        //then
        Truth.assertThat(actual).isEqualTo(1)
    }

    @Test
    fun getTypeDir() {
        //given
        val matcher = CheatProvider.getUriMatcher()
        val uri = Uri.parse("content://${CheatProvider.AUTHORITY}/data")

        //when
        val actual = matcher.match(uri)

        //then
        Truth.assertThat(actual).isEqualTo(0)
    }
}