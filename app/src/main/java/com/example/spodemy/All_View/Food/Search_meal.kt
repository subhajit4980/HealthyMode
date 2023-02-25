package com.example.spodemy.All_View.Food

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spodemy.databinding.FragmentSearchMealBinding

class Search_meal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentSearchMealBinding.inflate(inflater,container,false)
        return binding.root
    }

}