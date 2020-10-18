package com.novyapp.test.utlimatetodolist.providers

import android.content.Context
import androidx.annotation.StringRes

object ResourcesProvider {
    private var context: Context? = null

    fun provideResourceProvider(context: Context): ResourcesProvider {
        setContext(
            context
        )
        return this
    }

    private fun setContext(context: Context){
        ResourcesProvider.context = context
    }

    fun getString(@StringRes stringId: Int, vararg args: Any?): String{
        context?.let {
            return it.getString(stringId, *args)
        }
        return ""
    }
}