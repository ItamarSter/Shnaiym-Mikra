package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class AltStructs (
  @SerializedName("Parasha" ) var Parasha : Parasha? = Parasha()
)