package com.example.task2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task2.adapter.VenueAdapter
import com.example.task2.api.VenueViewModel
import com.example.task2.databinding.FragmentSavedMatchesBinding
import com.example.task2.model.Venue

class SavedMatchesFragment : Fragment() {

    private var _binding: FragmentSavedMatchesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VenueViewModel by activityViewModels()
    private lateinit var adapter: VenueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = VenueAdapter { venue ->
            val currentList = adapter.currentList.toMutableList()
            currentList.remove(venue)
            adapter.submitList(currentList)

            viewModel.deleteSavedVenue(venue)

            Toast.makeText(
                requireContext(),
                "${venue.name} removed from saved",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.recyclerViewSaved.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SavedMatchesFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.savedVenues.observe(viewLifecycleOwner) { venues ->
            updateUI(venues)
        }
    }

    private fun updateUI(venues: List<Venue>) {
        if (venues.isEmpty()) {
            binding.recyclerViewSaved.visibility = View.GONE
            binding.tvEmptySaved.visibility = View.VISIBLE
        } else {
            binding.recyclerViewSaved.visibility = View.VISIBLE
            binding.tvEmptySaved.visibility = View.GONE
            adapter.submitList(venues)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}