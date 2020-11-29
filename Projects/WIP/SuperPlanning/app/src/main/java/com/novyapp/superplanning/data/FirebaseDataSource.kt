package com.novyapp.superplanning.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import java.util.*

object FirebaseDataSource {

    private var database = Firebase.database.reference
    private var db = Firebase.firestore

    /**
     *  Returns a LinkedHashMap where
     *  LinkedHashMap at(WeekNumber) = mutable list of Courses this week
     *
     *  ==> LinkedHashMap<{WeekNumber},{listOfCourses}>
     */

    fun getAllCoursesByPromo(promo: String): MutableLiveData<LinkedHashMap<String, MutableList<Course>>> {
        val result = MutableLiveData(linkedMapOf<String, MutableList<Course>>())

        database.child("courses")
            .child(promo)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val toReturn = linkedMapOf<String, MutableList<Course>>()
                    snapshot.children.forEach { week ->
                        toReturn[week.key as String] = mutableListOf()
                        week.children.forEach { course ->
                            toReturn[week.key as String]!!.add(
                                course.getValue(Course::class.java) ?: Course()
                            )
                        }
                    }
                    Timber.i("Fetched data: $toReturn")
                    result.value = toReturn
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.i(error.message)
                }
            })
        return result
    }

    /**
     * @param course should be checked before this function
     */
    fun addCourseToPromo(
        course: CourseV2,
        promo: String = "TestPromo",
        resultLiveData: MutableLiveData<Result<String>>
    ) {
        val weekNumber = Calendar.getInstance()
            .also { it.timeInMillis = course.date!!.seconds * 1000 }
            .get(Calendar.WEEK_OF_YEAR)

        db.collection("Courses").document(promo)
            .set(mapOf("updated_at" to Timestamp(Date())), SetOptions.merge())
            .addOnSuccessListener {

                db.collection("/Courses/$promo/$weekNumber")
                    .add(course)
                    .addOnSuccessListener {
                        resultLiveData.value = Result.Success("Course added successfully")
                        Timber.i("upload: SUCCESS")
                    }
                    .addOnFailureListener { e ->
                        resultLiveData.value = Result.Error(e)
                        Timber.i("upload: FAILURE: $e")
                    }

            }
            .addOnFailureListener { e ->
                resultLiveData.value = Result.Error(e)
                Timber.i("upload: FAILURE: $e")
            }


    }


}