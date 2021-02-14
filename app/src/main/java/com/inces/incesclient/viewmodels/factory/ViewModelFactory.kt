package com.inces.incesclient.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inces.incesclient.repo.MainRepo
import com.inces.incesclient.viewmodels.MainViewModel

class ViewModelFactory(
    private val mainRepo: MainRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mainRepo) as T
    }
}