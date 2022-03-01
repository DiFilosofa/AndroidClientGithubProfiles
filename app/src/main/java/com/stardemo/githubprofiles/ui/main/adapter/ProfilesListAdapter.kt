package com.stardemo.githubprofiles.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stardemo.githubprofiles.data.Profile
import com.stardemo.githubprofiles.databinding.PartialProfileItemBinding

class ProfilesListAdapter : RecyclerView.Adapter<ProfilesListAdapter.ProfileViewHolder>() {

    var data: MutableList<Profile> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun insertProfiles(newProfiles: MutableList<Profile>) {
        val currentSize = data.size
        data.addAll(newProfiles)
        notifyItemRangeInserted(currentSize, newProfiles.size)
    }

    inner class ProfileViewHolder(
        private val binding: PartialProfileItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(profile: Profile) {
            binding.apply {
                Glide.with(itemView.context).load(profile.avatarUrl).into(ivUserAvatar)
                tvProfileName.text = profile.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = PartialProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount() = data.size
}