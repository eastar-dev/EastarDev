package dev.eastar.dev.ui

import android.app.Application
import android.log.Log
import androidx.lifecycle.AndroidViewModel
import dev.eastar.data.provider.CheatProvider

class MainActivityViewModel(private val app: Application) : AndroidViewModel(app) {
    fun getCheats(): Unit {
        Log.e("getCheats")

        app.contentResolver.query(CheatProvider.URI, null, null, null, null).use {
            Log.e(it?.columnCount,it?.count)
        }
    }
}