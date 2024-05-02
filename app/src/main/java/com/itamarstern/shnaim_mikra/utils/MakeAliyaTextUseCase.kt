package com.itamarstern.shnaim_mikra.utils

import com.itamarstern.shnaim_mikra.data.onkelosText
import com.itamarstern.shnaim_mikra.data.psukimOrPrakim
import com.itamarstern.shnaim_mikra.data.refs
import com.itamarstern.shnaim_mikra.data.torahText
import com.itamarstern.shnaim_mikra.module.StyledText
import javax.inject.Inject
import kotlin.text.StringBuilder

class MakeAliyaTextUseCase @Inject constructor() {

    fun makeText(bookIndex: Int, parashaIndex: Int, aliyaIndex: Int): ArrayList<StyledText> {
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
            makeOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList, torahText)
            aliyaTextList.add(StyledText("\n\nתרגום\n", true))
            makeOnePerekAliya(startPasukIndex, endPasukIndex, bookIndex, startPerekIndex, aliyaTextList, onkelosText)
        } else {
            makeMultiplePereksAliya(
                startPerekIndex,
                endPerekIndex,
                aliyaTextList,
                startPasukIndex,
                bookIndex,
                endPasukIndex,
                torahText
            )
            aliyaTextList.add(StyledText("\n\nתרגום\n", true))
            makeMultiplePereksAliya(
                startPerekIndex,
                endPerekIndex,
                aliyaTextList,
                startPasukIndex,
                bookIndex,
                endPasukIndex,
                onkelosText
            )
        }
        return aliyaTextList
    }

    private fun makeMultiplePereksAliya(
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

    private fun makeOnePerekAliya(
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