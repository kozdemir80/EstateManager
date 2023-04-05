package com.example.realestatemanager
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.realestatemanager.viewModel.Utils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*
@RunWith(AndroidJUnit4::class)
class RealEstateUnitTest {
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun checkConnectivity() {
        Assert.assertEquals(true, Utils.isInternetAvailable(appContext))
    }

    @Test
    fun checkDollarToEuroConversion() {
        Assert.assertEquals(81, Utils.convertDollarToEuro(100).toLong())
    }

    @Test
    fun checkEuroToDollarConversion() {
        Assert.assertEquals(100, Utils.convertEuroToDollar(81).toLong())
    }

    @Test
    fun checkDateFormat() {
        val date = SimpleDateFormat("dd/MM/yyyy").format(Date())
        Assert.assertEquals(date, Utils.todayDate)
    }
}