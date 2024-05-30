package com.example.shimmerrecyclerview
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class YourViewModel(application: Application) : AndroidViewModel(application) {
    private val networkConnectionLiveData = NetworkConnectionLiveData(application)

    fun getNetworkStatus(): LiveData<Boolean> {
        return networkConnectionLiveData
    }

    // Your other ViewModel code, like making API calls, etc.
}
