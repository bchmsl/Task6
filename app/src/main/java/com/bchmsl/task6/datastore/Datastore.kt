package com.bchmsl.task6.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bchmsl.task6.model.UserPreference
import kotlinx.coroutines.flow.Flow
import java.io.IOException

object Datastore {
    private const val DATASTORE_NAME = "userDataStore"
    val TOKEN_KEY = stringPreferencesKey("token")
    val REMEMBER_KEY = booleanPreferencesKey("remember")
    val Context.dataStore by preferencesDataStore(
        name = DATASTORE_NAME
    )

    suspend fun Context.clearDataStore() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun Context.removeUsername() {
        dataStore.edit { preference ->
            preference.remove(TOKEN_KEY)
        }
    }
}