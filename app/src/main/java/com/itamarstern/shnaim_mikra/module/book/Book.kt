package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class Book (
  @SerializedName("title"          ) var title          : String?           = null,
  @SerializedName("categories"     ) var categories     : ArrayList<String> = arrayListOf(),
  @SerializedName("schema"         ) var schema         : Schema?           = Schema(),
  @SerializedName("alt_structs"    ) var altStructs     : AltStructs?       = AltStructs(),
  @SerializedName("order"          ) var order          : ArrayList<Int>    = arrayListOf(),
  @SerializedName("authors"        ) var authors        : ArrayList<String> = arrayListOf(),
  @SerializedName("enDesc"         ) var enDesc         : String?           = null,
  @SerializedName("heDesc"         ) var heDesc         : String?           = null,
  @SerializedName("enShortDesc"    ) var enShortDesc    : String?           = null,
  @SerializedName("heShortDesc"    ) var heShortDesc    : String?           = null,
  @SerializedName("pubDate"        ) var pubDate        : ArrayList<Int>    = arrayListOf(),
  @SerializedName("hasErrorMargin" ) var hasErrorMargin : Boolean?          = null,
  @SerializedName("compDate"       ) var compDate       : ArrayList<Int>    = arrayListOf(),
  @SerializedName("compPlace"      ) var compPlace      : String?           = null,
  @SerializedName("pubPlace"       ) var pubPlace       : String?           = null,
  @SerializedName("is_cited"       ) var isCited        : Boolean?          = null,
  @SerializedName("corpora"        ) var corpora        : ArrayList<String> = arrayListOf()
)