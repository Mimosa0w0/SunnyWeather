package com.example.sunnyweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.FragmentPlaceBinding

class PlaceFragment: Fragment(R.layout.fragment_place) {

    private var binding: FragmentPlaceBinding? = null
    private val viewModel by lazy{ ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPlaceBinding.bind(view)

        val layoutManager = LinearLayoutManager(activity)
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding!!.recyclerView.layoutManager = layoutManager
        binding!!.recyclerView.adapter = adapter

        binding!!.searchPlaceEdit.addTextChangedListener{
            val content = it.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                binding!!.recyclerView.visibility = View.GONE
                binding!!.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner) {
            val place = it.getOrNull()
            if (place != null) {
                binding!!.recyclerView.visibility = View.VISIBLE
                binding!!.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(place)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}