package com.stardemo.githubprofiles.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stardemo.githubprofiles.data.interfaces.GithubProfileRepository
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubProfileViewModel @Inject constructor(
    private val repository: GithubProfileRepository
) : BaseViewModel() {

    val profileDetail by lazy { MutableLiveData<Profile>() }
    private var searchTerm = MutableLiveData("a") // initial search value so the list isn't empty by default

    val profiles = searchTerm.switchMap {
        repository.searchProfiles(it).cachedIn(viewModelScope)
    }

    fun searchParticipant(searchText: String) {
        showLoading.postValue(true)
        searchTerm.value = searchText
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
}