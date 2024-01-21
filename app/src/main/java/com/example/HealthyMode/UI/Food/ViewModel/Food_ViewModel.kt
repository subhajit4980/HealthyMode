package com.example.HealthyMode.UI.Food.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HealthyMode.FireStore.Repository.Repository
import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.data_Model.Nutrient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class Food_ViewModel @Inject constructor(val repository: Repository):ViewModel() {
    val mealTimes = listOf("breakfast", "morn_snack", "lunch", "evening", "dinner")
    private var _data = MutableLiveData<UIstate<Nutrient>>()
    val data: LiveData<UIstate<Nutrient>>
        get() = _data
    fun addFood(nutrient: Nutrient){
        _data.value = UIstate.Loading
        repository.addFood(nutrient) { _data.value = it }
    }
private val _calories = MutableLiveData<ArrayList<Float>>()
    val calories: LiveData<ArrayList<Float>> = _calories
    fun getCalories() {
        repository.getcalories(mealTimes).observeForever { calories ->
            _calories.postValue(calories)
        }
    }
    private val _nutrients = MutableLiveData<ArrayList<Float>>()
    val nutrients: LiveData<ArrayList<Float>>
        get() = _nutrients

    fun getNutrients() {
        viewModelScope.launch {
//            _nutrients.value = repository.getNutrients(mealTimes)
            repository.getNutrients(mealTimes).observeForever {nut->
                _nutrients.postValue(nut)
            }
        }
    }
}