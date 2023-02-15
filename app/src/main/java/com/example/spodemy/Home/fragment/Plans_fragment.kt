package com.example.spodemy.Home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spodemy.R
import com.example.spodemy.databinding.FragmentPlansFragmentBinding


class Plans_fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding=FragmentPlansFragmentBinding.inflate(inflater,container,false)
        binding.c1.setOnClickListener{ binding.tv.text = "subhajit" }

        return binding.root
    }
}