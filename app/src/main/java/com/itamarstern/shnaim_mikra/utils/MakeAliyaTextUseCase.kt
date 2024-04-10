package com.itamarstern.shnaim_mikra.utils

import com.itamarstern.shnaim_mikra.data.psukimOrPrakimNumbers
import com.itamarstern.shnaim_mikra.module.StyledText
import javax.inject.Inject
import kotlin.math.max
import kotlin.text.StringBuilder

class MakeAliyaTextUseCase @Inject constructor() {

    fun makeText(aliyaTextObject: ArrayList<Any>, ref: String): ArrayList<StyledText> {
        val firstPasukNumberIndex = ref.indexOf(':') + 1
        val makafIndex = max(ref.indexOf('-'), ref.indexOf('–'))
        val firstPasukNumber = ref.substring(firstPasukNumberIndex, makafIndex).toInt()
        val firstPerekNumber = ref.substring(ref.indexOf(' ') + 1, ref.indexOf(':')).toInt()
//        val secondPasukNumberIndex = max(ref.indexOf(':', firstPasukNumberIndex) + 1, makafIndex + 1)
//        val secondPasukNumber = ref.substring(secondPasukNumberIndex).toInt()

        val aliyaTextList = ArrayList<StyledText>()

        //if more than 1 prakim:
        if (aliyaTextObject[0] is ArrayList<*>) {
            for (i in aliyaTextObject.indices) {
                aliyaTextList.add(StyledText("פרק " + psukimOrPrakimNumbers[firstPerekNumber + i - 1] + "\n", true))

                val perekSB = StringBuilder()
                for (j in (aliyaTextObject[i] as ArrayList<*>).indices) {
                    if (i == 0) {
                        perekSB.append(psukimOrPrakimNumbers[j + firstPasukNumber - 1])
                    } else {
                        perekSB.append(psukimOrPrakimNumbers[j])
                    }
                    perekSB.append("${(aliyaTextObject[i] as ArrayList<*>)[j]}\n")
                }

                aliyaTextList.add(StyledText(perekSB.toString().removeHtml(), false))
            }
            // else: Aliya in just 1 perek:
        } else {
            aliyaTextList.add(StyledText("פרק " + psukimOrPrakimNumbers[firstPerekNumber - 1] + "\n", true))

            val perekSB = StringBuilder()
            for (i in aliyaTextObject.indices) {
                perekSB.append(psukimOrPrakimNumbers[i + firstPasukNumber - 1])
                perekSB.append("${aliyaTextObject[i]}\n")
            }

            aliyaTextList.add(StyledText(perekSB.toString().removeHtml(), false))
        }

        return aliyaTextList
    }
}

fun String.removeHtml() =
    this
        .replace(Regex("<[^>]+>"), "")
        .replace(Regex("&[a-zA-Z0-9]+;"), "")