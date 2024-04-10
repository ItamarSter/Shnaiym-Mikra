package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class MatchTemplates (

  @SerializedName("term_slugs" ) var termSlugs : ArrayList<String> = arrayListOf(),
  @SerializedName("scope"      ) var scope     : String?           = null

)