package com.stardemo.githubprofiles.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stardemo.githubprofiles.data.Profile
import com.stardemo.githubprofiles.data.Profiles
import com.stardemo.githubprofiles.data.interfaces.GithubProfileRepository
import com.stardemo.githubprofiles.data.repositories.GithubProfileRepoImpl
import com.stardemo.githubprofiles.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GithubProfileViewModel(
    private val repository: GithubProfileRepository = GithubProfileRepoImpl()
) : BaseViewModel() {

    val profilesList by lazy { MutableLiveData<Profiles>() }
    val profileDetail by lazy { MutableLiveData<Profile>() }
    private var searchTerm = ""
    private var currentProfilesPage: Int = 1

    private fun searchProfiles() = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.searchProfiles(searchTerm, maxProfilesPerPage, currentProfilesPage)
        // TODO: pagination
//        Log.e("eeeeeee", response.headers()["link"] ?: "")
        when {
            response.isSuccessful -> {
                showLoading.postValue(false)
                profilesList.postValue(response.body())
            }
            else -> {
                showLoading.postValue(false)
                profilesList.value?.items = mutableListOf()
                onError.postValue(null)
                Log.e("Error", response.message())
            }
        }
    }

    private var searchJob: Job? = null
    fun searchParticipant(searchText: String) {
        showLoading.postValue(true)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDelayMillis)
            searchTerm = searchText
            searchProfiles()
        }
    }

    fun cancelSearchJob() {
        searchJob?.cancel()
        showLoading.postValue(false)
    }

    fun getProfile(username: String) = viewModelScope.launch(Dispatchers.IO) {
        showLoading.postValue(true)
        val response = repository.getProfileByName(username)
        when {
            response.isSuccessful -> {
                showLoading.postValue(false)
                profileDetail.postValue(response.body())
            }
            else -> {
                showLoading.postValue(false)
                profileDetail.postValue(null)
                onError.postValue(null)
                Log.e("Error", response.message())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }

    companion object {
        const val maxProfilesPerPage = 30
        const val searchDelayMillis = 1000L
    }
}