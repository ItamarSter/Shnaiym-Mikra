package com.itamarstern.shnaim_mikra.data

import com.itamarstern.shnaim_mikra.module.aliya.Aliya
import com.itamarstern.shnaim_mikra.module.book.Book
import com.itamarstern.shnaim_mikra.network.ApiService
import javax.inject.Inject

class ShnaimMikraRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getBook(name: String): Result<Book> {
        return try {
            Result.success(apiService.getBook(name))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAliya(ref: String): Result<Aliya> {
        return try {
            Result.success(apiService.getAliya(ref))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOnkelos(ref: String): Result<Aliya> {
        return try {
            Result.success(apiService.getAliya("Onkelos $ref"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}