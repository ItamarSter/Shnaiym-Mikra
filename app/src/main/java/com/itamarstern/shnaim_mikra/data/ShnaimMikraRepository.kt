package com.itamarstern.shnaim_mikra.data

import com.itamarstern.shnaim_mikra.module.aliya.Aliya
import com.itamarstern.shnaim_mikra.module.book.Book
import com.itamarstern.shnaim_mikra.network.ApiService
import javax.inject.Inject

class ShnaimMikraRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getBook(name: String): Book {
        return apiService.getBook(name)
    }

    suspend fun getAliya(ref: String): Aliya {
        return apiService.getAliya(ref)
    }

    suspend fun getOnkelos(ref: String): Aliya {
        return apiService.getAliya("Onkelos $ref")
    }
}