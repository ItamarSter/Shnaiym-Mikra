package com.itamarstern.shnaim_mikra.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository @Inject constructor(
    private var dataStore: DataStore<Preferences>
) {

    suspend fun setAliyaDetails(
        bookIndex: Int,
        parashaIndex: Int,
        aliyaIndex: Int
    ) {
        dataStore.edit { preferences ->
            preferences[KEY_ALIYA_DETAILS] = "$bookIndex,$parashaIndex,$aliyaIndex"
        }
    }

    fun getAliyaDetailsFlow(): Flow<String> = dataStore.data
        .map { preferences ->
            preferences[KEY_ALIYA_DETAILS] ?: "0,0,0"
        }

    companion object {
        val KEY_ALIYA_DETAILS = stringPreferencesKey("key_aliya_details")
    }
}