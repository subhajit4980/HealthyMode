package com.example.HealthyMode.FireStore.Repository

import com.example.HealthyMode.Utils.UIstate
import com.example.HealthyMode.data_Model.weight
import com.github.mikephil.charting.data.Entry

interface Repository {
    fun getWeight(result: (UIstate<ArrayList<Entry>>) -> Unit)
    fun changedWeight(weight: weight, result: (UIstate<String>) -> Unit)

}