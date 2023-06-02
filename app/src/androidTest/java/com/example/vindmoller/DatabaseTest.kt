package com.example.vindmoller

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vindmoller.data.AppDatabase
import com.example.vindmoller.data.fylke.FylkeDao
import com.example.vindmoller.data.source.SourceDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var fylkeDao: FylkeDao
    private lateinit var sourceDao: SourceDao
    private lateinit var inventoryDatabase: AppDatabase

    // Set up the database and DAOs for testing
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        inventoryDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "item_database")
            .fallbackToDestructiveMigration()
            .createFromAsset("defaultDatabase9.db")
            .build()

        fylkeDao = inventoryDatabase.fylkeDao()
        sourceDao = inventoryDatabase.sourceDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        // Close the database after testing
        inventoryDatabase.close()
    }

    // Test the retrieval of all counties from the FylkeDao
    @Test
    @Throws(Exception::class)
    fun testGetAllCounties() = runBlocking {
        val alleFylker = fylkeDao.getAllCounties()
        val expectedFylkeListe = listOf("TRONDELAG", "AGDER", "VIKEN", "TROMS OG FINNMARK", "VESTFOLD OG TELEMARK", "MØRE OG ROMSDAL", "ROGALAND", "INNLANDET", "NORDLAND", "OSLO", "VESTLAND")
        val expectedValue = expectedFylkeListe.size
        assertEquals(expectedValue, alleFylker.size)

        // Assert that each retrieved county matches the expected county name
        for ((i, fylkeNavn) in expectedFylkeListe.withIndex()) {
            assertEquals(fylkeNavn, alleFylker[i].fylkeName)
        }
    }


    // Test the retrieval of all sources from the SourceDao
    @Test
    @Throws(Exception::class)
    fun testGetSources() = runBlocking {
        val alleSources = sourceDao.getSourceWithSourceHistory()
        val expectedValue = 40
        assertEquals(expectedValue, alleSources.size)
    }
}