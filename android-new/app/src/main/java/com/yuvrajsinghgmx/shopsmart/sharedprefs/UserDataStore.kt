/*package com.yuvrajsinghgmx.shopsmart.sharedprefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yuvrajsinghgmx.shopsmart.modelclass.UserRegistration
import com.yuvrajsinghgmx.shopsmart.screens.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by preferencesDataStore(name = "user_registration")

class UserDataStore(private val context: Context) {

    companion object {
        val FULL_NAME = stringPreferencesKey("full_name")
        val EMAIL = stringPreferencesKey("email")
        val ROLE = stringPreferencesKey("role")
        val PROFILE_URI = stringPreferencesKey("profile_uri")
    }

    suspend fun saveUser(user: UserRegistration) {
        context.userDataStore.edit { prefs ->
            prefs[FULL_NAME] = user.fullName
            prefs[EMAIL] = user.email ?: ""
            prefs[ROLE] = user.role.name
            prefs[PROFILE_URI] = user.profileImageUri ?: ""
        }
    }

    val userFlow: Flow<UserRegistration?> = context.userDataStore.data.map { prefs ->
        val fullName = prefs[FULL_NAME] ?: return@map null
        val role = prefs[ROLE]?.let { UserRole.valueOf(it) } ?: return@map null
        val email = prefs[EMAIL]?.takeIf { it.isNotEmpty() }
        val uri = prefs[PROFILE_URI]?.takeIf { it.isNotEmpty() }

        UserRegistration(fullName, email, role, uri)
    }
}*/

