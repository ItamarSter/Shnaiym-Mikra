package com.itamarstern.shnaim_mikra.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.itamarstern.shnaim_mikra.MainViewModel
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

    suspend fun setTargum(
        targum: MainViewModel.UiState.Targum
    ) {
        dataStore.edit { preferences ->
            preferences[KEY_TARGUM] = targum.name
        }
    }

    fun getTargumFlow(): Flow<MainViewModel.UiState.Targum> = dataStore.data
        .map { preferences ->
            if (preferences[KEY_TARGUM] == MainViewModel.UiState.Targum.RASHI.name) MainViewModel.UiState.Targum.RASHI
            else MainViewModel.UiState.Targum.ONKELOS
        }

    suspend fun setFontSize(
        fontSize: Int
    ) {
        dataStore.edit { preferences ->
            preferences[KEY_FONT_SIZE] = fontSize
        }
    }

    fun getFontSizeFlow(): Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[KEY_FONT_SIZE] ?: DEFAULT_FONT_SIZE
        }

    companion object {
        const val MAX_FONT_SIZE = 35
        const val MIN_FONT_SIZE = 10
        const val DEFAULT_FONT_SIZE = 14
        val KEY_ALIYA_DETAILS = stringPreferencesKey("key_aliya_details")
        val KEY_FONT_SIZE = intPreferencesKey("key_font_size")
        val KEY_TARGUM = stringPreferencesKey("key_targum")
    }
}