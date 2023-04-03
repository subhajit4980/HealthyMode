package com.example.HealthyMode.UI.Home_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.HealthyMode.UI.Home.Home_screen
import com.example.HealthyMode.UI.Reminder.MealReminder
import com.example.HealthyMode.UI.Reminder.Reminder
import com.example.HealthyMode.UI.Reminder.SanitizerReminder
import com.example.HealthyMode.UI.Task.Add_Task
import com.example.HealthyMode.UI.Task.TaskList
import com.example.HealthyMode.databinding.FragmentPlansFragmentBinding


class Plans_fragment : Fragment() {
 private lateinit var binding:FragmentPlansFragmentBinding

    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPlansFragmentBinding.inflate(inflater,container,false)
        getReminder()
        val childFragment: Fragment = TaskList()
        childFragmentManager.beginTransaction().apply {
            replace(com.example.HealthyMode.R.id.todolist, childFragment)
            commit()
        }
        binding.addPlan.setOnClickListener {
            startActivity(Intent(requireContext(),Add_Task::class.java))
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            startActivity(Intent(requireActivity(), Home_screen::class.java))
            requireActivity().finish()
        }
    }
    fun getReminder()
    {
        binding.apply {
            c1.setOnClickListener{
               startActivity(Intent(requireActivity(), Reminder::class.java))
            }
            c4.setOnClickListener{
               startActivity(Intent(requireActivity(), SanitizerReminder::class.java))
            }
            c2.setOnClickListener{
               startActivity(Intent(requireActivity(), MealReminder::class.java))
            }
            c3.setOnClickListener{
            }
        }
    }
    
}