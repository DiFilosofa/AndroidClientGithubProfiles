package com.stardemo.githubprofiles.ui.main.view

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.stardemo.githubprofiles.R
import com.stardemo.githubprofiles.data.Profile
import com.stardemo.githubprofiles.ui.viewmodel.GithubProfileViewModel
import com.stardemo.githubprofiles.databinding.ActivityProfileDetailBinding
import com.stardemo.githubprofiles.ui.base.BaseActivity
import com.stardemo.githubprofiles.ui.base.BaseViewModel

class ProfileDetailActivity : BaseActivity() {
    private lateinit var profileViewModel: GithubProfileViewModel
    private lateinit var binding: ActivityProfileDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(this)[GithubProfileViewModel::class.java]
        observeViewModel(profileViewModel)
        val username = intent.getStringExtra(KEY_USER_NAME)
        profileViewModel.getProfile(username ?: "")
        binding.btnBack.setOnClickListener { onBackPressed() }
    }

    override fun observeViewModel(viewModel: BaseViewModel) {
        super.observeViewModel(viewModel)
        profileViewModel.profileDetail.observe(this) {
            setProfileData(it)
        }
    }

    override fun showLoading(show: Boolean) {
        binding.vLoading.root.isVisible = show
    }

    private fun setProfileData(profile: Profile) {
        with(profile) {
            binding.tvFullName.text = name
            binding.tvUsername.text = getString(R.string.profile_username_initial).format(username)
            Glide.with(this@ProfileDetailActivity).load(avatarUrl)
                .placeholder(R.drawable.avatar_placeholder).into(binding.ivAvatar)
            binding.statFollowers.statValue = followersCount.toString()
            binding.statFollowing.statValue = followingCount.toString()
            binding.statRepos.statValue = publicReposCount.toString()
        }
    }

    companion object {
        const val KEY_USER_NAME = "keyUserName"
    }
}