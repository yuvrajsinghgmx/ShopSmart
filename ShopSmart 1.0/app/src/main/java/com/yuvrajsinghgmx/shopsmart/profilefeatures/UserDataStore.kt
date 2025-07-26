package com.yuvrajsinghgmx.shopsmart.profilefeatures

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException

// Extension property to create a DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_prefs")

// Keys for preferences
object UserPreferencesKeys {
    val NAME = stringPreferencesKey("user_name")
    val EMAIL = stringPreferencesKey("user_email")
    val PHONE = stringPreferencesKey("user_phone")
}

class UserDataStore(private val context: Context) {

    // Function to save user data
    fun saveUserData(name: String, email: String, phone: String) {
        // Use the IO dispatcher to avoid blocking the main thread
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { preferences ->
                preferences[UserPreferencesKeys.NAME] = name
                preferences[UserPreferencesKeys.EMAIL] = email
                preferences[UserPreferencesKeys.PHONE] = phone
            }
        }
    }

    // Function to retrieve user data as a Flow
    val userData: Flow<UserData> = context.dataStore.data
        .catch { exception ->
            // Handle IOExceptions during data retrieval
            if (exception is IOException) {
                emit(emptyPreferences()) // Emit empty preferences on error
            } else {
                throw exception // Re-throw other exceptions
            }
        }
        .map { preferences ->
            UserData(
                name = preferences[UserPreferencesKeys.NAME] ?: "",
                email = preferences[UserPreferencesKeys.EMAIL] ?: "",
                phone = preferences[UserPreferencesKeys.PHONE] ?: ""
            )
        }

    // Blocking call for immediate retrieval (if needed in non-suspend functions)
    fun getUserDataBlocking(): UserData = runBlocking {
        userData.first() // Retrieve the first emitted value
    }
}

// Data class to store user information
data class UserData(
    val name: String,
    val email: String,
    val phone: String
)
