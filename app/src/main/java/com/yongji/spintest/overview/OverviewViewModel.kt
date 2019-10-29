package com.yongji.spintest.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yongji.spintest.network.Food
import com.yongji.spintest.network.FoodsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<ApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<ApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsProperty
    // with new values
    private val _foodList = MutableLiveData<List<Food>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val foodList: LiveData<List<Food>>
        get() = _foodList

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedFoodItem = MutableLiveData<Food>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedFoodItem: LiveData<Food>
        get() = _navigateToSelectedFoodItem

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getAllFoods() on init so we can display status immediately.
     */
    init {
        getAllFoods()
    }

    /**
     * Gets food information from the Food API Retrofit service and
     * updates the [Food] [List] and [ApiStatus] [LiveData]. The Retrofit service
     * returns a coroutine Deferred, which we await to get the result of the transaction.
     */
    private fun getAllFoods() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getFoodListDeferred = FoodsApi.retrofitService.getFoods()
            try {
                _status.value = ApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getFoodListDeferred.await()
                _status.value = ApiStatus.DONE
                _foodList.value = listResult.data
                Log.e("yongjiSuccess", "yongjiSuccess")

            } catch (e: Exception) {
                Log.e("yongjiError", e.message.toString())
                _status.value = ApiStatus.ERROR
                _foodList.value = ArrayList()
            }
        }
    }

    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}