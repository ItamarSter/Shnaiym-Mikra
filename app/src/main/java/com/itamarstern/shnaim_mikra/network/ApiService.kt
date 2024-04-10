package com.itamarstern.shnaim_mikra.network

import com.itamarstern.shnaim_mikra.module.aliya.Aliya
import com.itamarstern.shnaim_mikra.module.book.Book
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("https://www.sefaria.org/api/v2/raw/index/{name}")
    suspend fun getBook(@Path("name") name: String): Book

    @GET("https://www.sefaria.org/api/v3/texts/{ref}")
    suspend fun getAliya(@Path("ref") ref: String): Aliya

}