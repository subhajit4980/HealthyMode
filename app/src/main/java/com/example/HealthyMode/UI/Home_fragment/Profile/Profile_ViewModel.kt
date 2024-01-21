package com.example.HealthyMode.UI.Home_fragment.Profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.HealthyMode.FireStore.Repository.Repository
import com.example.HealthyMode.Utils.UIstate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Profile_ViewModel @Inject constructor(private val repository: Repository):ViewModel(){
    fun updateHeight(height: String,context: Context) {
        repository.changeHeight(height,context)
    }
    private var _data = MutableLiveData<UIstate<String>>()
    val data: LiveData<UIstate<String>>
        get() = _data
    fun getHeight()
    {
        repository.getheight{
            _data.value = UIstate.Loading
            _data.value=it
        }
    }
}