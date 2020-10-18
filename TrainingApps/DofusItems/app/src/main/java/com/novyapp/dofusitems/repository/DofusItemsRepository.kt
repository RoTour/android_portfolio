package com.novyapp.dofusitems.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.novyapp.dofusitems.database.DofusItemsDatabase
import com.novyapp.dofusitems.database.asDomainDofusMounts
import com.novyapp.dofusitems.domain.DomainDofusMounts
import com.novyapp.dofusitems.network.Network
import com.novyapp.dofusitems.network.asDatabaseDofusMounts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception


class DofusItemsRepository(private val database: DofusItemsDatabase) {

    val dofusMounts: LiveData<List<DomainDofusMounts>> =
        Transformations.map(database.dofusItemsDatabaseDao.getAllDofusMounts()) {
            it.asDomainDofusMounts()
        }

    suspend fun refreshDofusItems() {
        withContext(Dispatchers.IO) {
            val mountsFromNetwork = Network.dofusItems.getMountsAsync()
            try{
                val result = mountsFromNetwork.await()
                database.dofusItemsDatabaseDao.insertAll(*result.asDatabaseDofusMounts())
            }catch (e:Exception){
                Timber.e(e)
            }
        }
    }

}