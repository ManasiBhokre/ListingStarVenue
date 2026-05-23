package com.example.task2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task2.R
import com.example.task2.databinding.ItemVenueBinding
import com.example.task2.model.Venue


class VenueAdapter(
    private val onStarClick: (Venue) -> Unit
) : ListAdapter<Venue, VenueAdapter.VenueViewHolder>(VenueDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val binding = ItemVenueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VenueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VenueViewHolder(
        private val binding: ItemVenueBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(venue: Venue) {
            binding.apply {
                tvVenueName.text = venue.name
                tvVenueAddress.text = venue.address
                tvVenueCategory.text = venue.category
                tvVenueDistance.text = venue.distance
                tvCheckins.text = "Checkins: ${venue.checkinsCount}"

                updateStarIcon(venue.isSaved)

                ibStar.setOnClickListener {
                    onStarClick(venue)
                }
            }
        }

        private fun updateStarIcon(isSaved: Boolean) {
            val iconRes = if (isSaved) R.drawable.ic_star_filled else R.drawable.ic_star_outlined
            val tintColor = if (isSaved) {
                ContextCompat.getColor(binding.root.context, R.color.star_active)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.star_inactive)
            }
            binding.ibStar.setImageResource(iconRes)
            binding.ibStar.setColorFilter(tintColor)
        }
    }

    class VenueDiffCallback : DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }
    }
}