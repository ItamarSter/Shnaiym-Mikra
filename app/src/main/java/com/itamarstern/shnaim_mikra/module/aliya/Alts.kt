package com.itamarstern.shnaim_mikra.module.aliya

import com.google.gson.annotations.SerializedName


data class Alts (

  @SerializedName("en"    ) var en    : ArrayList<String> = arrayListOf(),
  @SerializedName("he"    ) var he    : ArrayList<String> = arrayListOf(),
  @SerializedName("whole" ) var whole : Boolean?          = null

)