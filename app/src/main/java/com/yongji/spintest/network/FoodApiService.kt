package com.yongji.spintest.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.myjson.com/bins/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getFoods] method
 */
interface FoodApiService {
    /**
     * Returns a Coroutine [Deferred] [FoodApiResponse] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "n30si" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("n30si")
    fun getFoods():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<FoodApiResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object FoodsApi {
    val retrofitService : FoodApiService by lazy { retrofit.create(FoodApiService::class.java) }
}
