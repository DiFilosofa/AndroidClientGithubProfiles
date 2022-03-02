package com.stardemo.githubprofiles.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.stardemo.githubprofiles.R
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.ui.main.adapter.ProfilesListAdapter
import com.stardemo.githubprofiles.databinding.ActivitySearchBinding
import com.stardemo.githubprofiles.ui.base.BaseActivity
import com.stardemo.githubprofiles.ui.base.BaseViewModel
import com.stardemo.githubprofiles.ui.main.adapter.ProfileLoadStateAdapter
import com.stardemo.githubprofiles.ui.viewmodel.GithubProfileViewModel

class SearchActivity : BaseActivity() {

    private lateinit var profileViewModel: GithubProfileViewModel
    private lateinit var binding: ActivitySearchBinding
    private var profilesAdapter: ProfilesListAdapter? = null
    private lateinit var skeleton: Skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(this)[GithubProfileViewModel::class.java]
        observeViewModel(profileViewModel)
        setUpProfilesAdapter()
        setUpSearchView()
    }

    override fun observeViewModel(viewModel: BaseViewModel) {
        super.observeViewModel(viewModel)
        profileViewModel.profiles.observe(this) {
            setProfilesList(it)
        }
    }

    private fun setUpSearchView() {
        binding.searchView.apply {
            performClick()
            queryHint = getString(R.string.search_hint)
            isSubmitButtonEnabled = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrBlank()) {
                        profileViewModel.searchParticipant(query)
                        binding.searchView.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })
        }
    }

    private fun setUpProfilesAdapter() {
        profilesAdapter = ProfilesListAdapter(this::onProfileClicked).apply {
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        if (snapshot().isEmpty()) {
                            showLoading(true)
                        }
                        updateErrorTextVisibility(false)
                    }
                    is LoadState.NotLoading -> {
                        showLoading(false)
                        if (loadState.append.endOfPaginationReached && profilesAdapter!!.itemCount < 1) {
                            updateErrorTextVisibility(true, getString(R.string.no_user_found))
                        }
                    }
                    else -> {
                        showLoading(false)
                        handleLoadStatesError(loadState.source)
                    }
                }
            }
        }
        binding.rvProfilesList.apply {
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = profilesAdapter!!.withLoadStateHeaderAndFooter(
                header = ProfileLoadStateAdapter { profilesAdapter?.retry() },
                footer = ProfileLoadStateAdapter { profilesAdapter?.retry() }
            )
            skeleton = applySkeleton(R.layout.partial_profile_item)
        }
    }

    private fun handleLoadStatesError(loadStateSource: LoadStates) {
        val error = when {
            loadStateSource.prepend is LoadState.Error -> loadStateSource.prepend as LoadState.Error
            loadStateSource.append is LoadState.Error -> loadStateSource.append as LoadState.Error
            loadStateSource.refresh is LoadState.Error -> loadStateSource.refresh as LoadState.Error
            else -> null
        }
        error?.let {
            binding.rvProfilesList.isVisible = false
            updateErrorTextVisibility(true, it.error.localizedMessage)
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            skeleton.showSkeleton()
        } else {
            skeleton.showOriginal()
        }
    }

    private fun onProfileClicked(username: String) {
        startActivity(
            Intent(this, ProfileDetailActivity::class.java).apply {
                putExtra(ProfileDetailActivity.KEY_USER_NAME, username)
            }
        )
    }

    private fun setProfilesList(profiles: PagingData<Profile>) {
        profilesAdapter?.submitData(lifecycle, profiles)
        binding.rvProfilesList.isVisible = true
        skeleton.showOriginal()
    }

    private fun updateErrorTextVisibility(
        show: Boolean,
        errText: String? = getString(R.string.error_try_again)
    ) {
        binding.tvError.apply {
            isVisible = show
            text = errText
        }
    }
}