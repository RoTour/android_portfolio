package com.novyapp.test.fasttypingtraining.data.source.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


const val BASE_URL = "http://novyapp.ddns.net/novyapp/"

interface NovyappServer {
    @GET("ftta/usa_json.json")
    fun getWordListAsync(): Deferred<List<NetworkWord>>

    @GET("ftta/version.json")
    fun getWordListVersionAsync(): Deferred<NetworkListVersion>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val fttaApi: NovyappServer = retrofit.create(
        NovyappServer::class.java)
}