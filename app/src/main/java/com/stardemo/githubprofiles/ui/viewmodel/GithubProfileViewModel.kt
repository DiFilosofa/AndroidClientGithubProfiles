package com.stardemo.githubprofiles.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    private var searchTerm = ""
    var currentProfilesPage: Int = 1

    private fun searchProfiles() = viewModelScope.launch(Dispatchers.IO) {
        showLoading.postValue(true)
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
                onError.value = response.message()
                Log.e("Error", response.message())
            }
        }
    }

    private var searchJob: Job? = null
    fun searchParticipant(searchText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDelayMillis)
            searchTerm = searchText
            searchProfiles()
        }
    }

    fun cancelSearchJob() {
        searchJob?.cancel()
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