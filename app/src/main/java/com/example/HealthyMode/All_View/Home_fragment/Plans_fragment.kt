package com.example.HealthyMode.All_View.Home_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.HealthyMode.Adapter.ViewPagerAdapter
import com.example.HealthyMode.All_View.Home.Home_screen
import com.example.HealthyMode.All_View.Reminder.MealReminder
import com.example.HealthyMode.All_View.Reminder.Reminder
import com.example.HealthyMode.All_View.Reminder.SanitizerReminder
import com.example.HealthyMode.All_View.Task.AddTask
import com.example.HealthyMode.All_View.Task.TaskList
import com.example.HealthyMode.All_View.Task.ViewModel.TodoModel
import com.example.HealthyMode.TodoDatabase.Todo
import com.example.HealthyMode.databinding.FragmentPlansFragmentBinding
import com.google.android.material.tabs.TabLayout


class Plans_fragment : Fragment() {
 lateinit var todoModel: TodoModel
 private lateinit var todolist: ArrayList<Todo>
 private lateinit var binding:FragmentPlansFragmentBinding
    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentPlansFragmentBinding.inflate(inflater,container,false)
        getReminder()
        todolist= ArrayList<Todo>()
//        view model implementation
//        val dao=TodoDatabase.getDatabase(requireContext()).todoDao()
//        val repo= TodoRepository(dao)
//        todoModel=ViewModelProvider(this,ViewModelFactory(repo)).get(TodoModel::class.java)
//        todoModel.getTodo().observe(this, Observer { todo->
//            for (data in todo)
//            {
//                todolist.add(data)
//            }
//        })
        val tabLayout: TabLayout =binding.tabLayout
        val viewpager: ViewPager =binding.viewPager
        tabLayout.addTab(tabLayout.newTab().setText("Plans"))
        tabLayout.addTab(tabLayout.newTab().setText("Add Plans"))
        tabLayout.tabGravity= TabLayout.GRAVITY_FILL
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
        return binding.root
    }
    private fun setupViewPager(viewpager: ViewPager) {
        var adapter: ViewPagerAdapter =
            ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(TaskList(), "Plans")
        adapter.addFragment(AddTask(), "Add Plans")
        viewpager.setAdapter(adapter)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            startActivity(Intent(requireActivity(), Home_screen::class.java))
//            requireActivity().overridePendingTransition(R.anim.left_center, R.anim.right_center);
            requireActivity().finish()
        }
    }
    fun getReminder()
    {
        binding.apply {
            c1.setOnClickListener{
               startActivity(Intent(requireActivity(), Reminder::class.java))
            }
        }
        binding.apply {
            c4.setOnClickListener{
               startActivity(Intent(requireActivity(), SanitizerReminder::class.java))
            }
        }
        binding.apply {
            c2.setOnClickListener{
               startActivity(Intent(requireActivity(), MealReminder::class.java))
            }
        }
        binding.apply {
            c3.setOnClickListener{
            }
        }
    }
    
}