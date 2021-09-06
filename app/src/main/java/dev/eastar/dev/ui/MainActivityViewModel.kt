package dev.eastar.dev.ui

import android.log.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import dev.eastar.data.provider.CheatDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dao: CheatDao
) : ViewModel() {
    fun getCheats(): Unit {
        viewModelScope.launch {
            Log.e(dao)
            val items = dao.getItems()
            Log.e(items)
        }
        // app.contentResolver.query(CheatProvider.URI, null, null, null, null).use {
        //     Log.e(it?.columnCount, it?.count)
        // }
    }
}