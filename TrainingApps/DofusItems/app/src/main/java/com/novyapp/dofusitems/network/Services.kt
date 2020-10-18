package com.novyapp.dofusitems.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface Dofapi {
    @GET("mounts")
    fun getMountsAsync(): Deferred<List<NetworkDofusMounts>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fr.dofus.dofapi.fr/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val dofusItems: Dofapi = retrofit.create(Dofapi::class.java)
}
