package com.itamarstern.shnaim_mikra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itamarstern.shnaim_mikra.data.Parasha
import com.itamarstern.shnaim_mikra.data.aliyas
import com.itamarstern.shnaim_mikra.data.humashs
import com.itamarstern.shnaim_mikra.data.parashas
import com.itamarstern.shnaim_mikra.local.DataStoreRepository
import com.itamarstern.shnaim_mikra.local.SharedPreferencesRepository
import com.itamarstern.shnaim_mikra.module.StyledText
import com.itamarstern.shnaim_mikra.utils.MakeAliyaTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val makeAliyaText: MakeAliyaTextUseCase,
    private val userPreferences: DataStoreRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
): ViewModel() {

    private var bookIndex = 0
    private var parashaIndex = 0
    private var aliyaIndex = 0
    private var scrollOffset = 0
    private var isConnectedParashas = false

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferences.getAliyaDetailsFlow().collect {
                val details = it.split(',')
                bookIndex = details[0].toInt()
                parashaIndex = details[1].toInt()
                aliyaIndex = details[2].toInt()
                scrollOffset = sharedPreferencesRepository.getScrollOffset()

                updateAliya()

                updateBookName()
                updateParashaName()
                updateAliyaName()
            }
        }
        viewModelScope.launch {
            userPreferences.getFontSizeFlow().collect {
                _uiState.update { state ->
                    state.copy(fontSize = it)
                }
            }
        }

        viewModelScope.launch {
            userPreferences.getTargumFlow().collect { targum ->
                _uiState.update { state ->
                    state.copy(activeTargum = targum)
                }
                updateAliya()
            }
        }
    }

    private fun updateAliya() {
        _uiState.update { state ->
            state.copy(
                aliyaText = makeAliyaText.makeText(bookIndex, parashaIndex, aliyaIndex, state.activeTargum),
                state = UiState.State.Fetched,
                scrollOffset = scrollOffset
            )
        }
    }

    private fun updateBookName() {
        _uiState.update {
            it.copy(
                bookName = humashs[bookIndex]
            )
        }
    }

    fun saveScrollOffset(scrollOffset: Int = 0) {
        if (scrollOffset < 0) return

        sharedPreferencesRepository.saveScrollOffset(scrollOffset)
    }

    private fun saveAliyaDetails() {
        viewModelScope.launch {
            userPreferences.setAliyaDetails(bookIndex, parashaIndex, aliyaIndex)
        }
    }

    private fun onLoading() {
        _uiState.update { state ->
            state.copy(state = UiState.State.Loading)
        }
    }

    fun onBookForwardClick() {
        if (bookIndex < 4) {
            onLoading()
            bookIndex++
            resetParasha()
        }
    }

    fun onBookBackClick(goToLastParasha: Boolean = false, goToLastAliya: Boolean = false) {
        if (bookIndex > 0) {
            onLoading()
            bookIndex--
            resetParasha(if (goToLastParasha) parashas[bookIndex].size - 1 else parashaIndex, goToLastAliya)
        }
    }

    fun onParashaForwardClick() {
        if (parashaIndex < parashas[bookIndex].size - 1) {
            onLoading()
            parashaIndex++
            resetAliya()
        } else {
            onBookForwardClick()
        }
    }

    fun onParashaBackClick(goToLastAliya: Boolean = false) {
        if (parashaIndex > 0) {
            onLoading()
            parashaIndex--
            resetAliya(if (goToLastAliya) 6 else 0)
        } else {
            onBookBackClick(true, goToLastAliya)
        }
    }

    fun onAliyaForwardClick() {
        if (aliyaIndex < aliyas.size - 1) {
            onLoading()
            aliyaIndex++
            saveAliyaDetails()
        } else {
            onParashaForwardClick()
        }
    }

    fun onAliyaBackClick() {
        if (aliyaIndex > 0) {
            onLoading()
            aliyaIndex--
            saveAliyaDetails()
        } else {
            onParashaBackClick(true)
        }
    }

    private fun updateParashaName() {
        _uiState.update {
            it.copy(
                parashaName = if (isConnectedParashas) parashas[bookIndex][parashaIndex].nameIfConnected else parashas[bookIndex][parashaIndex].name,
                parasha = parashas[bookIndex][parashaIndex]
            )
        }
    }

    private fun updateAliyaName() {
        _uiState.update {
            it.copy(
                aliyaName = aliyas[aliyaIndex]
            )
        }
    }

    private fun resetParasha(parashaIndex: Int = 0, goToLastAliya: Boolean = false) {
        this.parashaIndex = parashaIndex
        resetAliya(if (goToLastAliya) 6 else 0)
    }

    private fun resetAliya(aliyaIndex: Int = 0) {
        this.aliyaIndex = aliyaIndex
        saveAliyaDetails()
    }

    fun onIncreaseFontClick() {
        viewModelScope.launch {
            if (uiState.value.fontSize < DataStoreRepository.MAX_FONT_SIZE)
                userPreferences.setFontSize(uiState.value.fontSize + 1)
        }
    }

    fun onDecreaseFontClick() {
        viewModelScope.launch {
            if (uiState.value.fontSize > DataStoreRepository.MIN_FONT_SIZE)
                userPreferences.setFontSize(uiState.value.fontSize - 1)
        }
    }

    fun onTargumChangeClicked() {
        viewModelScope.launch {
            userPreferences.setTargum(if (uiState.value.activeTargum == UiState.Targum.ONKELOS) UiState.Targum.RASHI else UiState.Targum.ONKELOS)
        }
    }

    data class UiState (
        var activeTargum: Targum = Targum.ONKELOS,
        var fontSize: Int = DataStoreRepository.DEFAULT_FONT_SIZE,
        var bookName: String = "",
        var parashaName: String = "",
        var aliyaName: String = "",
        var scrollOffset: Int = -1,
        var parasha: Parasha? = null,
        var aliyaText: ArrayList<StyledText> = arrayListOf(),
        var state: State = State.Loading
    ) {
        enum class State {
            Loading, Fetched, Failure
        }

        enum class Targum (val index: Int){
            RASHI(0), ONKELOS(1)
        }
    }
}

