package com.example.realestatemanager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.activities.EstateDataBase
import com.example.realestatemanager.model.EstateContentProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class ContentProviderTest{
    // FOR DATA
    private var mContentResolver: ContentResolver? = null
    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            EstateDataBase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }
    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ITEM,
                TEST_ID
            ), null, null, null, null
        )
        Assert.assertNotNull(cursor)
        Assert.assertEquals(cursor!!.count, 14)
        cursor.close()
    }
    @Test
    fun insertAndGetItem() {
        // BEFORE : Adding demo item
        mContentResolver!!.insert(EstateContentProvider.URI_ITEM, generateItem())
        // TEST
        val cursor = mContentResolver!!.query(ContentUris.withAppendedId(EstateContentProvider.URI_ITEM, TEST_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(15))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("realtor")), `is`("asdada"))
    }
    // ---
    private fun generateItem(): ContentValues {
        val values = ContentValues()
        values.put(DataTest.estate.realtor, "asdada")
        return values
    }
    companion object {
        // DATA SET FOR TEST
        private const val TEST_ID: Long = 1
    }
}