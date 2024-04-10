package com.itamarstern.shnaim_mikra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itamarstern.shnaim_mikra.module.book.Book
import com.itamarstern.shnaim_mikra.data.ShnaimMikraRepository
import com.itamarstern.shnaim_mikra.data.aliyas
import com.itamarstern.shnaim_mikra.data.humashs
import com.itamarstern.shnaim_mikra.data.parashas
import com.itamarstern.shnaim_mikra.local.DataStoreRepository
import com.itamarstern.shnaim_mikra.module.StyledText
import com.itamarstern.shnaim_mikra.module.aliya.Aliya
import com.itamarstern.shnaim_mikra.utils.MakeAliyaTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ShnaimMikraRepository,
    private val makeAliyaText: MakeAliyaTextUseCase,
    private val userPreferences: DataStoreRepository
): ViewModel() {

    private var bookIndex = 0
    private var parashaIndex = 0
    private var aliyaIndex = 0

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private var book: Book? = null
    private var aliya: Aliya? = null
    private var onkelos: Aliya? = null

    private val ref get() = book?.altStructs?.Parasha?.nodes?.get(parashaIndex)?.refs?.get(aliyaIndex)!!

    init {
        viewModelScope.launch {
            userPreferences.getAliyaDetailsFlow().collect {
                val indexes = it.split(',')
                bookIndex = indexes[0].toInt()
                parashaIndex = indexes[1].toInt()
                aliyaIndex = indexes[2].toInt()
                fetchBook(bookIndex)
                updateBookName()
                updateParashaName()
                updateAliyaName()
            }
        }
    }

    private fun fetchBook(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = (repository.getBook(humashs[index]))
            if (result.isSuccess) book = result.getOrNull()
            else onFetchFailure()
            updateBookName()
            fetchAliya()
        }
    }

    private fun onFetchFailure() {
        _uiState.update {  state ->
            state.copy(state = UiState.State.Failure)
        }
    }

    private fun updateBookName() {
        _uiState.update {
            it.copy(
                bookName = humashs[bookIndex],
    //                    state = UiState.State.Fetched
            )
        }
    }

    private fun updateAliya() {
        val text = ArrayList<StyledText>()
        text.addAll(makeAliyaText.makeText(aliya!!.versions[0].text, ref))
        text.add(StyledText("\n\nתרגום\n", true))
        text.addAll(makeAliyaText.makeText(onkelos!!.versions[0].text, ref))
        _uiState.update {
            it.copy(
                aliyaText = text,
                state = UiState.State.Fetched
            )
        }
    }

    private fun fetchAliya() {
        viewModelScope.launch(Dispatchers.IO) {
            val aliyaResult = repository.getAliya(ref)
            val onkelosResult = repository.getOnkelos(ref)
            if (aliyaResult.isFailure || onkelosResult.isFailure) {
                onFetchFailure()
            } else {
                aliya = aliyaResult.getOrNull()
                onkelos = onkelosResult.getOrNull()
                updateAliya()
            }
        }
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

    fun onBookBackClick() {
        if (bookIndex > 0) {
            onLoading()
            bookIndex--
            resetParasha()
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

    fun onParashaBackClick() {
        if (parashaIndex > 0) {
            onLoading()
            parashaIndex--
            resetAliya()
        } else {
            onBookBackClick()
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
            onParashaBackClick()
        }
    }

    private fun updateParashaName() {
        _uiState.update {
            it.copy(
                parashaName = parashas[bookIndex][parashaIndex]
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

    private fun resetParasha() {
        parashaIndex = 0
        resetAliya()
    }

    private fun resetAliya() {
        aliyaIndex = 0
        saveAliyaDetails()
    }

    data class UiState (
        var bookName: String = "",
        var parashaName: String = "",
        var aliyaName: String = "",
        var aliyaText: ArrayList<StyledText> = arrayListOf(),
        var state: State = State.Loading
    ) {
        enum class State {
            Loading, Fetched, Failure
        }
    }
}

