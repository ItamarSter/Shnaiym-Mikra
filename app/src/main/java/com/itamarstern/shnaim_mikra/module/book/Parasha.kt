package com.itamarstern.shnaim_mikra.module.book

import com.google.gson.annotations.SerializedName


data class Parasha (
  @SerializedName("nodes" ) var nodes : ArrayList<Nodes> = arrayListOf()
)