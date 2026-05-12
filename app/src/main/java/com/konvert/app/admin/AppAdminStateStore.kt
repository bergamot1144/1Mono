package com.konvert.app.admin

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.appAdminStateDataStore by preferencesDataStore(name = "app_admin_state")

class AppAdminStateStore(context: Context) {
    private val dataStore = context.applicationContext.appAdminStateDataStore

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun load(): AppAdminState {
        return dataStore.data
            .map { prefs ->
                val raw = prefs[AdminStateJsonKey] ?: return@map AppAdminState()
                runCatching { json.decodeFromString<AppAdminState>(raw) }
                    .getOrElse { error ->
                        if (error is SerializationException || error is IllegalArgumentException) {
                            AppAdminState()
                        } else {
                            throw error
                        }
                    }
            }
            .first()
    }

    suspend fun save(state: AppAdminState) {
        val raw = json.encodeToString(state)
        dataStore.edit { prefs ->
            prefs[AdminStateJsonKey] = raw
        }
    }

    private companion object {
        val AdminStateJsonKey = stringPreferencesKey("admin_state_json")
    }
}
