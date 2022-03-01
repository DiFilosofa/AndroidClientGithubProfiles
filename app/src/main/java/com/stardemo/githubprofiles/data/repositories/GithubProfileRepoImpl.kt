package com.stardemo.githubprofiles.data.repositories

import com.stardemo.githubprofiles.data.interfaces.GithubProfileRepository
import com.stardemo.githubprofiles.data.services.GithubRetrofitService

class GithubProfileRepoImpl : GithubProfileRepository {
    private val retrofitClient by lazy { GithubRetrofitService.getInstance() }

    override suspend fun searchProfiles(query: String, itemsPerPage: Int, page: Int) =
        retrofitClient.getProfilesList(query, itemsPerPage, page)

    override suspend fun getProfileByName(username: String) =
        retrofitClient.getProfilesByName(username)
}