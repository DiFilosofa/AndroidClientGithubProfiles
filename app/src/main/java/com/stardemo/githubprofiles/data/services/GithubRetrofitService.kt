package com.stardemo.githubprofiles.data.services

import com.stardemo.githubprofiles.BuildConfig
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.data.model.Profiles
import com.stardemo.githubprofiles.utils.BeautifyHttpLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubRetrofitService {
    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getProfilesList(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Profiles

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getProfilesByName(@Path("username") username: String): Response<Profile>
}