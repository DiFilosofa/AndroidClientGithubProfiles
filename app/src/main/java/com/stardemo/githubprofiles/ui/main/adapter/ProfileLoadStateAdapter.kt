package com.stardemo.githubprofiles.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stardemo.githubprofiles.databinding.PartialProfileLoadStateBinding

class ProfileLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ProfileLoadStateAdapter.ProfileLoadStateVH>() {

    inner class ProfileLoadStateVH(
        private val binding: PartialProfileLoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            with(binding) {
                if (loadState is LoadState.Error) {
                    tvError.text = loadState.error.localizedMessage
                }
                pbLoading.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                tvError.isVisible = loadState is LoadState.Error
                btnRetry.setOnClickListener { retry.invoke() }
            }
        }

    }

    override fun onBindViewHolder(holder: ProfileLoadStateVH, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ProfileLoadStateVH {
        val binding = PartialProfileLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileLoadStateVH(binding)
    }
}