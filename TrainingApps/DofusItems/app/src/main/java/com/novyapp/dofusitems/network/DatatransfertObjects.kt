package com.novyapp.dofusitems.network

import android.os.Parcelable
import com.novyapp.dofusitems.database.DatabaseDofusMounts
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NetworkDofusMounts(
    @Json(name = "_id") val id: Double,
    val ankamaId: Double,
    val name: String,
    val level: Double,
    val type: String,
    val imgUrl: String

    /** Example of a mount object
     * 	{
     *  "_id": 1,
     *  "ankamaId": 1,
     *  "name": "Dragodinde Amande Sauvage",
     *  "level": 100,
     *  "type": "Montures",
     *  "imgUrl": "https://s.ankama.com/www/static.ankama.com/dofus/renderer/look/7b3633397c7c313d31363737323034352c323d31363737323034352c333d31363737323034352c343d373735383931357c3132307d/full/1/200_200-10.png",
     *  "url": "https://www.dofus.com/fr/mmorpg/encyclopedie/montures/1-dragodinde-amande-sauvage"
     *  },
     *
     */
) : Parcelable


//fun List<NetworkDofusMounts>.asDomainDofusMounts(): List<DomainDofusMounts>{
//    return map {
//        DomainDofusMounts(
//            id = it.id,
//            ankamaId =  it.ankamaId,
//            name = it.name,
//            level = it.level,
//            imgUrl = it.imgUrl,
//            type = it.type)
//    }
//}

fun List<NetworkDofusMounts>.asDatabaseDofusMounts(): Array<DatabaseDofusMounts>{
    return map {
        DatabaseDofusMounts(
            id = it.id,
            ankamaId =  it.ankamaId,
            name = it.name,
            level = it.level,
            imgUrl = it.imgUrl,
            type = it.type)
    }.toTypedArray()
}