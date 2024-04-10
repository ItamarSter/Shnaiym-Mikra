package com.itamarstern.shnaim_mikra.di

import com.itamarstern.shnaim_mikra.AppClass
import com.itamarstern.shnaim_mikra.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideMyApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}