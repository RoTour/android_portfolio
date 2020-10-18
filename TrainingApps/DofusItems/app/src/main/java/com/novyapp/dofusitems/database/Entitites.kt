package com.novyapp.dofusitems.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novyapp.dofusitems.domain.DomainDofusMounts

@Entity(tableName = "dofus_mounts_table")
data class DatabaseDofusMounts(
    @PrimaryKey val id: Double,
    val ankamaId: Double,
    val name: String,
    val level: Double,
    val type: String,
    val imgUrl: String
)

fun List<DatabaseDofusMounts>.asDomainDofusMounts(): List<DomainDofusMounts>{
    return map {
        DomainDofusMounts(
            id = it.id,
            ankamaId =  it.ankamaId,
            name = it.name,
            level = it.level,
            imgUrl = it.imgUrl,
            type = it.type)
    }
}