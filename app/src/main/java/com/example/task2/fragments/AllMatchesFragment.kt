package com.example.task2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task2.adapter.VenueAdapter
import com.example.task2.api.UiState
import com.example.task2.api.VenueViewModel
import com.example.task2.databinding.FragmentAllMatchesBinding
import com.example.task2.model.Venue

class AllMatchesFragment : Fragment() {

    private var _binding: FragmentAllMatchesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VenueViewModel by activityViewModels()
    private lateinit var adapter: VenueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupSwipeRefresh()
    }

    private fun setupRecyclerView() {
        adapter = VenueAdapter { venue ->
            viewModel.toggleSave(venue)
            val msg = if (venue.isSaved) "Removed from saved" else "Saved!"
            Toast.makeText(requireContext(), "${venue.name}: $msg", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewAllMatches.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllMatchesFragment.adapter  // FIXED: Uncommented this line
        }
    }

    private fun observeViewModel() {
        viewModel.allVenuesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showVenues(state.venues)
                is UiState.Error   -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerViewAllMatches.visibility = View.GONE
            tvError.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showVenues(venues: List<Venue>) {
        binding.apply {
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            if (venues.isEmpty()) {
                recyclerViewAllMatches.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                tvError.text = "No venues found."
            } else {
                recyclerViewAllMatches.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                adapter.submitList(venues)
            }
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            recyclerViewAllMatches.visibility = View.GONE
            tvError.visibility = View.VISIBLE
            tvError.text = "Error: $message"
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadVenues()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}