package com.stardemo.githubprofiles.data

import com.google.gson.annotations.SerializedName

data class Profiles(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") var items: MutableList<Profile>
)

data class Profile(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatarUrl: String? = "",
    @SerializedName("name") val name: String,
    @SerializedName("company") val companyName: String,
    @SerializedName("followers") val followersCount: Int,
    @SerializedName("public_repos") val publicReposCount: Int,
    @SerializedName("following") val followingCount: Int
)