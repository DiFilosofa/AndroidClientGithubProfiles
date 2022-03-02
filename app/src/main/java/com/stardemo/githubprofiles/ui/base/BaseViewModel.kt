package com.stardemo.githubprofiles.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    val showLoading by lazy { MutableLiveData<Boolean>() }
    val onError by lazy { MutableLiveData<Any>() }
}