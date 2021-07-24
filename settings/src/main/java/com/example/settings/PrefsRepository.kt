package com.example.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrefsRepository @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val webServiceUrlKey = context.getString(R.string.web_service_url_key)
    private val importKey = context.getString(R.string.import_key)
    private val defaultWebServiceUrl =
        context.getString(R.string.web_service_url_default)

    suspend fun loadWebServiceUrl(): String = withContext(Dispatchers.IO) {
        prefs.getString(webServiceUrlKey, defaultWebServiceUrl) ?: defaultWebServiceUrl
    }

    fun observeImportChanges() = channelFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (importKey == key) {
                trySend(prefs.getBoolean(importKey, false))
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}
