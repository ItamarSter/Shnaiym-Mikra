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
            book = repository.getBook(humashs[index])
            updateBookName()
            fetchAliya()
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
                aliyaText = text
            )
        }
    }

    private fun fetchAliya() {
        viewModelScope.launch(Dispatchers.IO) {
            aliya = repository.getAliya(ref)
            onkelos = repository.getOnkelos(ref)
            updateAliya()
        }
    }

    fun onBookBackClick() {
        if (bookIndex < 4) bookIndex++
        resetParasha()
    }

    private fun saveAliyaDetails() {
        viewModelScope.launch {
            userPreferences.setAliyaDetails(bookIndex, parashaIndex, aliyaIndex)
        }
    }

    fun onBookForwardClick() {
        if (bookIndex > 0) bookIndex--
        resetParasha()
    }

    fun onParashaBackClick() {
        if (parashaIndex < parashas[bookIndex].size - 1) {
            parashaIndex++
            resetAliya()
        }
    }

    fun onParashaForwardClick() {
        if (parashaIndex > 0) {
            parashaIndex--
            resetAliya()
        }
    }

    private fun updateParashaName() {
        _uiState.update {
            it.copy(
                parashaName = parashas[bookIndex][parashaIndex]
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

    fun onAliyaBackClick() {
        if (aliyaIndex < aliyas.size - 1) {
            aliyaIndex++
            saveAliyaDetails()
        }
    }

    fun onAliyaForwardClick() {
        if (aliyaIndex > 0) {
            aliyaIndex--
            saveAliyaDetails()
        }
    }

    private fun updateAliyaName() {
        _uiState.update {
            it.copy(
                aliyaName = aliyas[aliyaIndex]
            )
        }
    }

    data class UiState (
        var bookName: String = "",
        var parashaName: String = "",
        var aliyaName: String = "",
        var aliyaText: ArrayList<StyledText> = arrayListOf(),
        var state: State = State.Loading
    ) {
        enum class State {
            Loading, Fetched
        }
    }
}

