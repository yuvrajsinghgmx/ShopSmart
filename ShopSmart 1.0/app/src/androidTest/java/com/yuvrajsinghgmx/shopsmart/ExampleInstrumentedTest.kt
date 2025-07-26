package com.yuvrajsinghgmx.shopsmart

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumented tests for the ShopSmart application.
 * These tests will run on an Android device or emulator.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Get the application context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Assert that the package name is as expected.
        assertEquals("com.yuvrajsinghgmx.shopsmart", appContext.packageName)
    }

    @Test
    fun checkAppName() {
        // Verify that the app name is correct.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appName = appContext.getString(R.string.app_name) // Assuming app_name is defined in strings.xml
        assertEquals("ShopSmart", appName) // Replace with your actual app name
    }

    @Test
    fun checkResourceAvailability() {
        // Check if a specific resource (like a drawable or string) is available.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val resourceId = appContext.resources.getIdentifier("ic_launcher", "drawable", appContext.packageName)
        assertTrue("Resource not found", resourceId != 0) // Check if resource exists
    }
}