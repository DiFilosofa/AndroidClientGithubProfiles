package com.stardemo.githubprofiles.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.stardemo.githubprofiles.data.interfaces.GithubProfileRepository
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.data.remote.RemoteDataSource
import com.stardemo.githubprofiles.data.services.GithubRetrofitService
import javax.inject.Inject

class GithubProfileRepoImpl @Inject constructor(
    private val retrofitClient: GithubRetrofitService
): GithubProfileRepository {

    override fun searchProfiles(query: String): LiveData<PagingData<Profile>> =
        Pager(config = PagingConfig(
                pageSize = MAX_ITEMS_PER_PAGE,
                enablePlaceholders = false,
            ), pagingSourceFactory = { RemoteDataSource(retrofitClient, query) }
        ).liveData

    override suspend fun getProfileByName(username: String) =
        retrofitClient.getProfilesByName(username)

    companion object {
        const val MAX_ITEMS_PER_PAGE = 30
    }
}