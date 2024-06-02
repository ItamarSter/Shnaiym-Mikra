package com.itamarstern.shnaim_mikra.utils

import com.itamarstern.shnaim_mikra.MainViewModel
import com.itamarstern.shnaim_mikra.data.onkelosText
import com.itamarstern.shnaim_mikra.data.psukimOrPrakim
import com.itamarstern.shnaim_mikra.data.rashiBamidbar
import com.itamarstern.shnaim_mikra.data.rashiBereshit
import com.itamarstern.shnaim_mikra.data.rashiDvarim
import com.itamarstern.shnaim_mikra.data.rashiShmot
import com.itamarstern.shnaim_mikra.data.rashiVaykra
import com.itamarstern.shnaim_mikra.data.refs
import com.itamarstern.shnaim_mikra.data.torahText
import com.itamarstern.shnaim_mikra.module.StyledText
import javax.inject.Inject
import kotlin.text.StringBuilder

class MakeAliyaTextUseCase @Inject constructor() {

    fun makeText(bookIndex: Int, parashaIndex: Int, aliyaIndex: Int, targum: MainViewModel.UiState.Targum): ArrayList<StyledText> {
        val ref = refs[bookIndex][parashaIndex][aliyaIndex]
        val startPart = ref.substring(0, ref.indexOf('-'))
        val endPart = ref.substring(ref.indexOf('-') + 1)
        val startPerekIndex = startPart.substring(0, startPart.indexOf(':')).toInt() - 1
        val endPerekIndex = endPart.substring(0, endPart.indexOf(':')).toInt() - 1
        val startPasukIndex = startPart.substring(startPart.indexOf(':') + 1).toInt() - 1
        val endPasukIndex = endPart.substring(endPart.indexOf(':') + 1).toInt() - 1

        val aliyaTextList = ArrayList<StyledText>()

        if (startPerekIndex == endPerekIndex) {
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakim[startPerekIndex] + "\n", true))
            makeOnkelosOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList, torahText)
            aliyaTextList.add(StyledText("\n\n", false))
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakim[startPerekIndex] + "\n", true))
            makeOnkelosOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList, torahText)
            aliyaTextList.add(StyledText("\n\nתרגום\n", true))
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakim[startPerekIndex] + "\n", true))
            when (targum) {
                MainViewModel.UiState.Targum.RASHI -> {
                    makeRashiOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList)
                }
                MainViewModel.UiState.Targum.ONKELOS -> {
                    makeOnkelosOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList, onkelosText)
                }
            }
        } else {
            makeOnkelosMultiplePereksAliya(
                startPerekIndex,
                endPerekIndex,
                aliyaTextList,
                startPasukIndex,
                bookIndex,
                endPasukIndex,
                torahText
            )
            aliyaTextList.add(StyledText("\n\n", false))
            makeOnkelosMultiplePereksAliya(
                startPerekIndex,
                endPerekIndex,
                aliyaTextList,
                startPasukIndex,
                bookIndex,
                endPasukIndex,
                torahText
            )
            aliyaTextList.add(StyledText("\n\nתרגום\n", true))
            when (targum) {
                MainViewModel.UiState.Targum.RASHI -> {
                    makeRashiMultiplePereksAliya(
                        startPerekIndex,
                        endPerekIndex,
                        aliyaTextList,
                        startPasukIndex,
                        bookIndex,
                        endPasukIndex
                    )
                }
                MainViewModel.UiState.Targum.ONKELOS -> {
                    makeOnkelosMultiplePereksAliya(
                        startPerekIndex,
                        endPerekIndex,
                        aliyaTextList,
                        startPasukIndex,
                        bookIndex,
                        endPasukIndex,
                        onkelosText
                    )
                }
            }
        }
        return aliyaTextList
    }

    private fun makeOnkelosMultiplePereksAliya(
        startPerekIndex: Int,
        endPerekIndex: Int,
        aliyaTextList: ArrayList<StyledText>,
        startPasukIndex: Int,
        bookIndex: Int,
        endPasukIndex: Int,
        textSource: List<List<List<String>>>
    ) {
        for (i in startPerekIndex..endPerekIndex) {
            val perekSB = StringBuilder()
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakim[i] + "\n", true))
            if (i == startPerekIndex) {
                for (j in startPasukIndex until textSource[bookIndex][i].size) {
                    perekSB.append(psukimOrPrakim[j])
                    perekSB.append("${textSource[bookIndex][i][j]}\n")
                }
            } else if (i == endPerekIndex) {
                for (j in 0..endPasukIndex) {
                    perekSB.append(psukimOrPrakim[j])
                    perekSB.append("${textSource[bookIndex][i][j]}\n")
                }
            } else {
                for (j in 0 until textSource[bookIndex][i].size) {
                    perekSB.append(psukimOrPrakim[j])
                    perekSB.append("${textSource[bookIndex][i][j]}\n")
                }
            }
            aliyaTextList.add(StyledText(perekSB.toString().removeHtml(), false))
        }
    }

    private fun makeRashiMultiplePereksAliya(
        startPerekIndex: Int,
        endPerekIndex: Int,
        aliyaTextList: ArrayList<StyledText>,
        startPasukIndex: Int,
        bookIndex: Int,
        endPasukIndex: Int
    ) {
        var rashi = listOf(listOf(listOf<String>()))
        when (bookIndex) {
            0 -> rashi = rashiBereshit
            1 -> rashi = rashiShmot
            2 -> rashi = rashiVaykra
            3 -> rashi = rashiBamidbar
            4 -> rashi = rashiDvarim
        }
        for (i in startPerekIndex..endPerekIndex) {
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakim[i] + "\n", true))
            if (i == startPerekIndex) {
                for (j in startPasukIndex until rashi[bookIndex][i].size) {
                    if (rashi[i][j].isNotEmpty()) {
                        aliyaTextList.add(StyledText(psukimOrPrakim[j] + "\n", false))
                        for (rashiDiburHamathil in rashi[i][j]) {
                            val regex = Regex("</b>")
                            val matchResult = regex.find(rashiDiburHamathil)
                            val startOfRashiItselfIndex = matchResult!!.range.last + 1
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(0, startOfRashiItselfIndex).removeHtml(), true))
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(startOfRashiItselfIndex).removeHtml() + "\n", false))
                        }
                    }
                }
            } else if (i == endPerekIndex) {
                for (j in 0..endPasukIndex) {
                    if (rashi[i][j].isNotEmpty()) {
                        aliyaTextList.add(StyledText(psukimOrPrakim[j] + "\n", false))
                        for (rashiDiburHamathil in rashi[i][j]) {
                            val regex = Regex("</b>")
                            val matchResult = regex.find(rashiDiburHamathil)
                            val startOfRashiItselfIndex = matchResult!!.range.last + 1
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(0, startOfRashiItselfIndex).removeHtml(), true))
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(startOfRashiItselfIndex).removeHtml() + "\n", false))
                        }
                    }
                }
            } else {
                for (j in 0 until rashi[bookIndex][i].size) {
                    if (rashi[i][j].isNotEmpty()) {
                        aliyaTextList.add(StyledText(psukimOrPrakim[j] + "\n", false))
                        for (rashiDiburHamathil in rashi[i][j]) {
                            val regex = Regex("</b>")
                            val matchResult = regex.find(rashiDiburHamathil)
                            val startOfRashiItselfIndex = matchResult!!.range.last + 1
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(0, startOfRashiItselfIndex).removeHtml(), true))
                            aliyaTextList.add(StyledText(rashiDiburHamathil.substring(startOfRashiItselfIndex).removeHtml() + "\n", false))
                        }
                    }
                }
            }
        }
    }

    private fun makeRashiOnePerekAliya(
        startPasukIndex: Int,
        endPasukIndex: Int,
        bookIndex: Int,
        startPerekIndex: Int,
        aliyaTextList: ArrayList<StyledText>
    ) {
        var rashi = listOf(listOf(listOf<String>()))
        when (bookIndex) {
            0 -> rashi = rashiBereshit
            1 -> rashi = rashiShmot
            2 -> rashi = rashiVaykra
            3 -> rashi = rashiBamidbar
            4 -> rashi = rashiDvarim
        }
        for (i in startPasukIndex..endPasukIndex) {
            if (rashi[startPerekIndex][i].isNotEmpty()) {
                aliyaTextList.add(StyledText(psukimOrPrakim[i] + "\n", false))
                for (rashiDiburHamathil in rashi[startPerekIndex][i]) {
                    val regex = Regex("</b>")
                    val matchResult = regex.find(rashiDiburHamathil)
                    val startOfRashiItselfIndex = matchResult!!.range.last + 1
                    aliyaTextList.add(StyledText(rashiDiburHamathil.substring(0, startOfRashiItselfIndex).removeHtml(), true))
                    aliyaTextList.add(StyledText(rashiDiburHamathil.substring(startOfRashiItselfIndex).removeHtml() + "\n", false))
                }
            }
        }
    }

    private fun makeOnkelosOnePerekAliya(
        startPasukIndex: Int,
        endPasukIndex: Int,
        bookIndex: Int,
        startPerekIndex: Int,
        aliyaTextList: ArrayList<StyledText>,
        textSource: List<List<List<String>>>
    ) {
        val perekSB = StringBuilder()
        for (i in startPasukIndex..endPasukIndex) {
            perekSB.append(psukimOrPrakim[i])
            perekSB.append("${textSource[bookIndex][startPerekIndex][i]}\n")
        }
        aliyaTextList.add(StyledText(perekSB.toString().removeHtml(), false))
    }
}

fun String.removeHtml() =
    this
        .replace("&nbsp;", " ")
        .replace(Regex("<[^>]+>"), "")
        .replace(Regex("&[a-zA-Z0-9]+;"), "")