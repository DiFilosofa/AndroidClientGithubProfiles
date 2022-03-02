package com.stardemo.githubprofiles.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.stardemo.githubprofiles.R
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.ui.main.adapter.ProfilesListAdapter
import com.stardemo.githubprofiles.databinding.ActivitySearchBinding
import com.stardemo.githubprofiles.ui.base.BaseActivity
import com.stardemo.githubprofiles.ui.base.BaseViewModel
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
        profileViewModel.profilesList.observe(this) {
            setProfilesList(it.items)
        }
    }

    private fun setUpSearchView() {
        binding.searchView.apply {
            performClick()
            queryHint = getString(R.string.search_hint)
            isSubmitButtonEnabled = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    updateNoDataTextVisibility(show = false)
                    if (newText.isNullOrBlank()) {
                        setProfilesList(mutableListOf())
                        profileViewModel.cancelSearchJob()
                    } else {
                        profileViewModel.searchParticipant(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun setUpProfilesAdapter() {
        profilesAdapter = ProfilesListAdapter(this::onProfileClicked)
        binding.rvProfilesList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
            adapter = profilesAdapter
            skeleton = applySkeleton(R.layout.partial_profile_item)
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

    private fun setProfilesList(profiles: MutableList<Profile>) {
        updateNoDataTextVisibility(show = profiles.isEmpty())
        profilesAdapter?.data = profiles
    }

    private fun updateNoDataTextVisibility(show: Boolean) {
        binding.tvNoData.isVisible = show
    }
}