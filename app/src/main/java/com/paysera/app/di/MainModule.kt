package com.paysera.app.di

import android.content.Context
import com.paysera.app.BuildConfig
import com.paysera.app.db.AppDatabase
import com.paysera.app.domain.network.CurrencyExchangeRateApi
import com.paysera.app.domain.network.adapters.ErrorsCallAdapterFactory
import com.paysera.app.domain.repositories.ExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@InstallIn(SingletonComponent::class)
@Module
class MainModule {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PayseraHost


    @Provides
    @Singleton
    @PayseraHost
    fun providePayseraHost(@ApplicationContext context: Context): String = BuildConfig.PAYSERA_HOST

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()


    @Provides
    @Singleton
    fun providesMyApi(
        @PayseraHost host: String,
        httpClient: OkHttpClient
    ): CurrencyExchangeRateApi {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(host)
            .addCallAdapterFactory(ErrorsCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyExchangeRateApi::class.java)
    }

    @Provides
    @Singleton
    fun providesRoomStorage(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePayseraCurrencyRepository(api: CurrencyExchangeRateApi, db: AppDatabase) =
        ExchangeRateRepository(api, db)
}
