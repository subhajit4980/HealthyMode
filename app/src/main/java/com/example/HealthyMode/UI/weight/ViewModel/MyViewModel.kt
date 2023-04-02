package com.example.HealthyMode.UI.weight.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.HealthyMode.FireStore.Repository.Repository
import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.data_Model.weight
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MyViewModel @Inject constructor(val fire_store: Repository): ViewModel() {
    private var _data = MutableLiveData<UIstate<ArrayList<Entry>>>()
    val data: LiveData<UIstate<ArrayList<Entry>>>
        get() = _data
    fun getWeight() {
        fire_store.getWeight {
            _data.value = UIstate.Loading
            _data.value = it
        }
    }
    private val _weight=MutableLiveData<UIstate<String>>()
    val weight: LiveData<UIstate<String>>
    get() = _weight
    fun changedWeight(Weight: weight)
    {
        fire_store.changedWeight(Weight){
            _weight.value=UIstate.Loading
            _weight.value=it
        }
    }
}