package com.homesoftwaretools.toptalproperty.repo.googlemap

import com.google.android.gms.maps.GoogleMap
import com.google.gson.GsonBuilder
import com.homesoftwaretools.toptalproperty.domain.Location
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.GeocodeRepo
import com.squareup.okhttp.Response
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.HttpURLConnection

class GoogleGeocodeRepo : GeocodeRepo {

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://maps.googleapis.com")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    val api: GeocodeApi = retrofit.create(GeocodeApi::class.java)

    override fun decode(address: String, key: String): Single<Location> =
        api.geocode(address, key)
            .subscribeOn(Schedulers.io())
            .map {
                logd { "gecode ${it.body()}" }
                Location(0.0, 0.0)
            }
            .doOnError { logd { "error $it" } }

}

interface GeocodeApi {
    @GET("/maps/api/geocode/json")
    fun geocode(@Query("address") location: String, @Query("key") apiKey: String): Single<Response>
}