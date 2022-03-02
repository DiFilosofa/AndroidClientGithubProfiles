package com.stardemo.githubprofiles.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stardemo.githubprofiles.R
import com.stardemo.githubprofiles.data.model.Profile
import com.stardemo.githubprofiles.databinding.PartialProfileItemBinding

class ProfilesListAdapter(
    private val onItemClicked: (String) -> Unit
) : PagingDataAdapter<Profile, ProfilesListAdapter.ProfileViewHolder>(PROFILE_DIFF_CALLBACK) {

    inner class ProfileViewHolder(
        private val binding: PartialProfileItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(profile: Profile) {
            binding.apply {
                Glide.with(itemView.context).load(profile.avatarUrl)
                    .placeholder(R.drawable.avatar_placeholder).into(ivUserAvatar)
                tvProfileName.text = profile.username
                root.setOnClickListener {
                    onItemClicked.invoke(profile.username)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding =
            PartialProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        getItem(position)?.run {
            holder.bindData(this)
        }
    }

    companion object {
        private val PROFILE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Profile>() {
            override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem == newItem
            }

        }
    }
}