package com.example.exercisetracking.UI.Fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exercisetracking.R
import com.example.exercisetracking.RunSortType
import com.example.exercisetracking.UI.AppViewModel
import com.example.exercisetracking.adapter.ActAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_running.*

/**
 * Created by Nhat Huy Giang
 * Stydent ID: 20313345
 * Email: psyng1@nottingham.ac.uk
 */
@AndroidEntryPoint // inject request data through Dagger Hilt into Fragment
class RunningFragment: Fragment(R.layout.fragment_running ) {

    private val viewModel : AppViewModel by viewModels()

    private lateinit var runAdapter: ActAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()

        when(viewModel.runSortType){
            RunSortType.DATE -> optionSort.setSelection(0)
            RunSortType.RUN_TIME -> optionSort.setSelection(1)
            RunSortType.DISTANCE -> optionSort.setSelection(2)
            RunSortType.SPEED -> optionSort.setSelection(3)
        }
        optionSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos){
                    0 -> viewModel.sortRuns(RunSortType.DATE)
                    1 -> viewModel.sortRuns(RunSortType.RUN_TIME)
                    2 -> viewModel.sortRuns(RunSortType.DISTANCE)
                    3 -> viewModel.sortRuns(RunSortType.SPEED)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        viewModel.runActs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        startYourRun.setOnClickListener{
            findNavController().navigate(R.id.action_runningFragment_to_runTrackingFragment)
        }
    }

    private fun setUpView() = runList.apply {
        runAdapter = ActAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}