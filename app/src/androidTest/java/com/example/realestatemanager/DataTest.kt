@file:Suppress("DEPRECATION")
package com.example.realestatemanager
import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.realestatemanager.dao.EstateDao
import com.example.realestatemanager.dao.EstateDataBase
import com.example.realestatemanager.model.EstateData
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
class DataTest {
    private lateinit var db: EstateDataBase
    private lateinit var dao: EstateDao
    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    @Throws(Exception::class)
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EstateDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.estateDao()
    }
    @After
    @Throws(Exception::class)
    fun tearDown() {
        db.close()
    }
    @Test
    fun addEstateAndReadAllData() = runBlocking {
        dao.addEstate(estate)
        val allEstates = dao.readData().getOrAwaitValue()
        org.junit.Assert.assertEquals(allEstates.size, 1)
        Assert.assertEquals(allEstates[0], estate)
    }
    companion object {
        val estate = EstateData().apply {
            id = 1
            type = "House"
            district = "NeverLand"
            price = 200000
            description = "a dream place"
            surface = 200
            realtor = "asdada"
            status = "Vacant"
            rooms = 3
            bedrooms = 1
            bathrooms= 1
            address = "1600 Amphitheatre Parkway, Mountain View, CA"
            entryDate = "04 april 2023"
            saleDate = "Not sold yet"
            vicinity = arrayListOf("Mall", "restaurants", "stores")
            photoUris =
                arrayListOf("https://www.pexels.com/photo/modern-building-against-sky-323780/")
            photoCaptions = arrayListOf("Beautiful view")
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)
        try {
            afterObserve.invoke()

            // Don't wait indefinitely if LiveData isn't set
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set")
            }
        } finally {
            this.removeObserver(observer)
        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}