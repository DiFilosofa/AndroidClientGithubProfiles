package com.stardemo.githubprofiles.data.interfaces

import com.stardemo.githubprofiles.data.Profile
import com.stardemo.githubprofiles.data.Profiles
import retrofit2.Response

interface GithubProfileRepository {
    suspend fun searchProfiles(query: String, itemsPerPage: Int, page: Int): Response<Profiles>
    suspend fun getProfileByName(username: String): Response<Profile>
}