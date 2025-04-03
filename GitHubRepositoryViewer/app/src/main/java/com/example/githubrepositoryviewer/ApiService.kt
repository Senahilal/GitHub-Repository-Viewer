//ApiService.kt

package com.example.githubrepositoryviewer

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.Path

interface ApiService {
    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String, //path parameter, not a query string
        @Query("per_page") perPage: Int = 10, //fetch 10 repos per page - load more click
        @Query("page") page: Int = 1
    ): List<GitHubRepo>
}

object ApiClient {
    private const val BASE_URL = "https://api.github.com/"

    //from lecture example
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Explicitly add KotlinJsonAdapterFactory
        .build() // Build the Moshi instance

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
