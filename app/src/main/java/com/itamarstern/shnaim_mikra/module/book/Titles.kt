package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class Titles (

  @SerializedName("text" ) var text : String? = null,
  @SerializedName("lang" ) var lang : String? = null

)