package com.novyapp.dofusitems.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DofusItemsDatabaseDao {
    @Query("select * from dofus_mounts_table")
    fun getAllDofusMounts(): LiveData<List<DatabaseDofusMounts>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dofusMounts: DatabaseDofusMounts)
}

@Database(entities = [DatabaseDofusMounts::class], version = 1)
abstract class DofusItemsDatabase : RoomDatabase(){
    abstract val dofusItemsDatabaseDao: DofusItemsDatabaseDao
}

private lateinit var INSTANCE: DofusItemsDatabase

fun getDatabase(context: Context): DofusItemsDatabase{
    synchronized(DofusItemsDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context,
                DofusItemsDatabase::class.java,
                "dofus_items_database"
            ).build()
        }
    }
    return INSTANCE
}