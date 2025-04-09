// SearchViewModel.kt
package com.example.buslive.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableLiveData<Triple<String, String, String>>()
    val searchQuery: LiveData<Triple<String, String, String>> = _searchQuery

    fun setSearchQuery(from: String, to: String, date: String) {
        _searchQuery.value = Triple(from, to, date)
    }
}
