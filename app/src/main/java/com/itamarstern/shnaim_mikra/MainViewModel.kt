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
    private var targum = UiState.Targum.ONKELOS

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            userPreferences.getAliyaDetailsFlow().collect {
                val details = it.split(',')
                bookIndex = details[0].toInt()
                parashaIndex = details[1].toInt()
                aliyaIndex = details[2].toInt()
                isConnectedParashas = details[3].toInt() == 1
                targum = when (details[4].toInt()) {
                    UiState.Targum.ONKELOS.index -> UiState.Targum.ONKELOS
                    UiState.Targum.RASHI.index -> UiState.Targum.RASHI
                    else -> UiState.Targum.ONKELOS
                }
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
    }

    private fun updateAliya() {
        _uiState.update { state ->
            state.copy(
                activeTargum = targum,
                isConnected = isConnectedParashas,
                aliyaText = makeAliyaText.makeText(bookIndex, parashaIndex, aliyaIndex, targum, isConnectedParashas && parashas[bookIndex][parashaIndex].canBeConnected),
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
            userPreferences.setAliyaDetails(bookIndex, parashaIndex, aliyaIndex, isConnectedParashas, targum)
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
            resetParasha(if (goToLastParasha) parashas[bookIndex].size - 1 else 0, goToLastAliya)
        }
    }

    fun onParashaForwardClick() {
        if (parashaIndex < parashas[bookIndex].size - 1) {
            onLoading()
            parashaIndex++
            if (isConnectedParashas &&
                parashas[bookIndex][parashaIndex].canBeConnected &&
                parashas[bookIndex][parashaIndex - 1].canBeConnected &&
                parashas[bookIndex][parashaIndex].connectedIndex ==
                parashas[bookIndex][parashaIndex - 1].connectedIndex) {
                onParashaForwardClick()
                return
            }
            resetAliya()
        } else {
            onBookForwardClick()
        }
    }

    fun onParashaBackClick(goToLastAliya: Boolean = false) {
        if (parashaIndex > 0) {
            onLoading()
            parashaIndex--
            if (isConnectedParashas &&
                parashas[bookIndex][parashaIndex].canBeConnected &&
                parashas[bookIndex][parashaIndex + 1].canBeConnected &&
                parashas[bookIndex][parashaIndex].connectedIndex ==
                parashas[bookIndex][parashaIndex + 1].connectedIndex) {
                onParashaBackClick()
                return
            }
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
                parashaName = if (isConnectedParashas && parashas[bookIndex][parashaIndex].canBeConnected) parashas[bookIndex][parashaIndex].nameIfConnected else parashas[bookIndex][parashaIndex].name,
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
        targum = if (targum == UiState.Targum.ONKELOS) UiState.Targum.RASHI else UiState.Targum.ONKELOS
        saveAliyaDetails()
    }

    fun connectedParashasClicked() {
        isConnectedParashas = !isConnectedParashas
        saveAliyaDetails()
    }

    data class UiState (
        var isConnected: Boolean = false,
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

