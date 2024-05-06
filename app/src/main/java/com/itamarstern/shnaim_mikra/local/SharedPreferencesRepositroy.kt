package com.itamarstern.shnaim_mikra.local

import android.content.SharedPreferences
import javax.inject.Inject

const val SCROLL_OFFSET_KEY = "scroll_offset_key"

class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveScrollOffset(scrollOffset: Int) {
        saveInt(SCROLL_OFFSET_KEY, scrollOffset)
    }

    fun getScrollOffset(): Int {
        return getInt(SCROLL_OFFSET_KEY)
    }

    private fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }
}