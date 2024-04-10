package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class Nodes (

    @SerializedName("nodeType"        ) var nodeType       : String?                   = null,
    @SerializedName("depth"           ) var depth          : Int?                      = null,
    @SerializedName("wholeRef"        ) var wholeRef       : String?                   = null,
    @SerializedName("addressTypes"    ) var addressTypes   : ArrayList<String>         = arrayListOf(),
    @SerializedName("sectionNames"    ) var sectionNames   : ArrayList<String>         = arrayListOf(),
    @SerializedName("refs"            ) var refs           : ArrayList<String>         = arrayListOf(),
    @SerializedName("match_templates" ) var matchTemplates : ArrayList<MatchTemplates> = arrayListOf(),
    @SerializedName("sharedTitle"     ) var sharedTitle    : String?                   = null

)