package com.example.HealthyMode.Utils

sealed class UIstate<out T> {
    object Loading: UIstate<Nothing>()
    data class Success<out T>(val data: T): UIstate<T>()
    data class Failure(val error: String?): UIstate<Nothing>()
}