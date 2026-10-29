package com.example.data.remote

import com.example.data.model.GutendexResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GutendexApi {
    @GET("books/")
    suspend fun getPopularBooks(
        @Query("page") page: Int = 1
    ): GutendexResponse

    @GET("books/")
    suspend fun searchBooks(
        @Query("search") query: String,
        @Query("page") page: Int = 1
    ): GutendexResponse

    @GET("books/")
    suspend fun getBooksByTopic(
        @Query("topic") topic: String,
        @Query("page") page: Int = 1
    ): GutendexResponse

    companion object {
        private const val BASE_URL = "https://gutendex.com/"

        fun create(): GutendexApi {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(GutendexApi::class.java)
        }
    }
}
