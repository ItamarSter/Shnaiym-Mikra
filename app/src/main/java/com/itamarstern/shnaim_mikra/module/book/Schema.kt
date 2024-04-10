package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class Schema (

    @SerializedName("nodeType"        ) var nodeType       : String?                   = null,
    @SerializedName("depth"           ) var depth          : Int?                      = null,
    @SerializedName("addressTypes"    ) var addressTypes   : ArrayList<String>         = arrayListOf(),
    @SerializedName("sectionNames"    ) var sectionNames   : ArrayList<String>         = arrayListOf(),
    @SerializedName("match_templates" ) var matchTemplates : ArrayList<MatchTemplates> = arrayListOf(),
    @SerializedName("lengths"         ) var lengths        : ArrayList<Int>            = arrayListOf(),
    @SerializedName("titles"          ) var titles         : ArrayList<Titles>         = arrayListOf(),
    @SerializedName("key"             ) var key            : String?                   = null

)