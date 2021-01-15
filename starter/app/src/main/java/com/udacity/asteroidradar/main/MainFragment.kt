package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener { selectedAsteroid ->
            viewModel.navigateToSelectedAsteroid(selectedAsteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer { selectedAsteroid ->
            selectedAsteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(selectedAsteroid))
                viewModel.navigateToSelectedAsteroidDone()
            }
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer { pictureOfDay ->
            pictureOfDay?.let {
                binding.pictureOfDay = pictureOfDay
            }
        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { newAsteroidList ->
            Timber.i("submitList")
            newAsteroidList?.let {
                adapter.submitList(newAsteroidList)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week  -> viewModel.onUpdateFilterToThisWeek()
            R.id.show_today -> viewModel.onUpdateFilterToToday()
            else            -> viewModel.onUpdateFilterToSaved()
        }
        return true
    }
}
