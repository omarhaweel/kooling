package com.example.vindmoller

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vindmoller.data.AppDatabase
import com.example.vindmoller.data.windturbines.WindTurbine
import com.example.vindmoller.data.windturbines.WindTurbineDao
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WindTurbineTest {
    private lateinit var windTurbineDao: WindTurbineDao
    private lateinit var inventoryDatabase: AppDatabase
    private val turbine1 = WindTurbine(1, sourceId = "SN18700", timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis()))
    private val turbine2 = WindTurbine(2, sourceId = "SN18700", timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis()))

    // Create an in-memory Room database for testing
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        inventoryDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
            .fallbackToDestructiveMigration()
            .createFromAsset("defaultDatabase9.db")
            .build()

        windTurbineDao = inventoryDatabase.windTurbineDao()
    }

    // Close the database after testing
    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    // Add one wind turbine item to the database
    private suspend fun addOneItemToDb() {
        windTurbineDao.insertWindTurbine(turbine1)
    }

    // Add two wind turbine items to the database
    private suspend fun addTwoItemsToDb() {
        windTurbineDao.insertWindTurbine(turbine1)
        windTurbineDao.insertWindTurbine(turbine2)
    }

    //Test inserting windmills into the database and getting it returnes the same windmill
    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsWindTurbineIntoDB(): Unit = runBlocking {
        // Remove wind turbines with the same source ID from the database
        windTurbineDao.removeSourceTurbines(sourceId = "SN18700")

        // Add one wind turbine to the database
        addOneItemToDb()

        // Add one wind turbine to the database
        val firstItem = windTurbineDao.getAllWindTurbines().first()

        // Check if the retrieved item match the expected values
        assertEquals(turbine1.id, firstItem.id)
        assertEquals(turbine1.sourceId, firstItem.sourceId)
        assertEquals(turbine1.timestamp.toString().subSequence(0, 19), firstItem.timestamp.toString().subSequence(0, 19))
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        // Remove wind turbines with the same source ID from the database, should just be this source windmills have been inserted into
        windTurbineDao.removeSourceTurbines(sourceId = "SN18700")

        // Add two wind turbines to the database
        addTwoItemsToDb()

        // Retrieve all items from the database
        val twoItems = windTurbineDao.getAllWindTurbines()

        // Check if the retrieved items match the expected values
        assertEquals(turbine1.id, twoItems[0].id)
        assertEquals(turbine1.sourceId, twoItems[0].sourceId)
        assertEquals(turbine1.timestamp.toString().subSequence(0, 19), twoItems[0].timestamp.toString().subSequence(0, 19))

        assertEquals(turbine2.id, twoItems[1].id)
        assertEquals(turbine2.sourceId, twoItems[1].sourceId)
        assertEquals(turbine2.timestamp.toString().subSequence(0, 19), twoItems[1].timestamp.toString().subSequence(0, 19))
    }

}