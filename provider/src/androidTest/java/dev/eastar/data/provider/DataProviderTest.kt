package dev.eastar.data.provider

import android.content.ContentValues
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class DataProviderTest {

    @Test
    fun insert() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dev.eastar.data.provider.test", appContext.packageName)

        val uri = Uri.parse("content://${DataProvider.AUTHORITY}/data/id")
        appContext.contentResolver.insert(uri, ContentValues().apply {

        })
    }

    @Test
    fun query() {
    }
}