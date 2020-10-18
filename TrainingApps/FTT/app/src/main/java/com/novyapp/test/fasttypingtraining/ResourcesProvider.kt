package com.novyapp.test.fasttypingtraining

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat

object ResourcesProvider {

    var context: Context? = null
        @VisibleForTesting set

    private fun setResourcesContext(ctx: Context){
        context = ctx
    }

    fun provideResourceProvider(context: Context): ResourcesProvider{
        setResourcesContext(context)
        return this
    }

    fun provideContext(context: Context): Context{
        return context
    }

    fun provideColor(@ColorRes colorId: Int): Int {
        context?.let {context ->
            return ContextCompat.getColor(context, colorId)
        }
        return Color.BLACK
    }

    fun provideString(@StringRes stringId: Int, vararg args: Any?): String {
        context?.let {context ->
            return context.getString(stringId, *args)
        }
        return ""
    }

}