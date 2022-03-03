package com.stardemo.githubprofiles.ui.base

import com.stardemo.githubprofiles.BuildConfig
import com.stardemo.githubprofiles.data.interfaces.GithubProfileRepository
import com.stardemo.githubprofiles.data.repositories.GithubProfileRepoImpl
import com.stardemo.githubprofiles.data.services.GithubRetrofitService
import com.stardemo.githubprofiles.utils.BeautifyHttpLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(repository: GithubProfileRepoImpl): GithubProfileRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideGithubRetrofitService(): GithubRetrofitService {
        val interceptor = HttpLoggingInterceptor(BeautifyHttpLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(GithubRetrofitService::class.java)
    }

}