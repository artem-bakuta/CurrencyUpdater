package com.paysera.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paysera.app.db.dao.models.ExchangeRateDaoModel
import com.paysera.app.domain.repositories.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ExchangeRateViewModel @Inject constructor(private val repository: ExchangeRateRepository) :
    ViewModel(), DefaultLifecycleObserver {

    private val _latestRate = MutableLiveData<ExchangeRateDaoModel>()
    val latestRate: LiveData<ExchangeRateDaoModel> = _latestRate
    var job: Job

    init {
        job = fetchLatestRates()
    }

    private fun fetchLatestRates() = viewModelScope.launch(Dispatchers.IO) {
        while (isActive) {
            try {
                val latestRates = repository.getLatestRates()
                withContext(Dispatchers.Main) { _latestRate.value = latestRates }
            } catch (e: Exception) {
                Log.e("ExchangeRateViewModel", "fetchLatestRates:${e.message}")
                requestCachedData()
            }
            delay(REPEAT_TIME)
        }
    }

    private fun requestCachedData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val latestRates = repository.getLatestRatesCached()
                withContext(Dispatchers.Main) { _latestRate.value = latestRates }
            } catch (e: Exception) {
                Log.e("ExchangeRateViewModel", "requestCachedData ${e.message}")
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (job.isActive.not()) job = fetchLatestRates()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        job.cancel()
    }


    companion object {
        val REPEAT_TIME = 5.seconds
    }
}

