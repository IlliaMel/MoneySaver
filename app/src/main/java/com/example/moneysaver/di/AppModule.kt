package com.example.moneysaver.di

import com.example.moneysaver.data.remote.CurrencyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
//&base=USD&symbols=EUR,USD,UAH,GBP,CNY,AED,TRY,BTC
    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl("https://api.exchangerate.host")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}