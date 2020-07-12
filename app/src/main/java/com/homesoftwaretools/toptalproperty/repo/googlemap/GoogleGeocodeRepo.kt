package com.homesoftwaretools.toptalproperty.repo.googlemap

import com.google.gson.GsonBuilder
import com.homesoftwaretools.toptalproperty.domain.Location
import com.homesoftwaretools.toptalproperty.logd
import com.homesoftwaretools.toptalproperty.repo.GeocodeRepo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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
                it.results.firstOrNull()?.geometry?.location?.let { loc ->
                    Location(loc.lat, loc.lng)
                } ?: throw Exception("Address not found")
            }

}

class GeocodeResults(
    val results: List<GeocodeResult>
)

class GeocodeResult(
    val geometry: GeocodeGeometry
)

class GeocodeGeometry(
    val location: GeocodeLocation
)

class GeocodeLocation(
    val lat: Double,
    val lng: Double
)

interface GeocodeApi {
    @GET("/maps/api/geocode/json")
    fun geocode(@Query("address") location: String, @Query("key") apiKey: String): Single<GeocodeResults>
}