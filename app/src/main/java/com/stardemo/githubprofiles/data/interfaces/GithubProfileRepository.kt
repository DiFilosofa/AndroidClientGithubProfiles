package com.stardemo.githubprofiles.data.interfaces

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.stardemo.githubprofiles.data.model.Profile
import retrofit2.Response

interface GithubProfileRepository {
    fun searchProfiles(query: String): LiveData<PagingData<Profile>>
    suspend fun getProfileByName(username: String): Response<Profile>
}