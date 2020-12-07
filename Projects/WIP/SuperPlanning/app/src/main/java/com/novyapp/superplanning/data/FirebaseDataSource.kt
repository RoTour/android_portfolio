package com.novyapp.superplanning.data

import androidx.lifecycle.MutableLiveData
import com.google.common.base.Ascii.toUpperCase
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.todayWeekNumber
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

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
     * @param weekNumber to select from which week you want to get the data
     * @return : HashMap of WeekNumber => mutableListOfCoursesThisWeek
     */

    fun getCoursesByPromo(
        promo: String,
        weekNumber: String = todayWeekNumber().toString()
    ): MutableLiveData<HashMap<String, MutableList<CourseV2>>> {

        val result = MutableLiveData<HashMap<String, MutableList<CourseV2>>>()
        val resultBuilder = HashMap<String, MutableList<CourseV2>>()

        db.collection("/Courses/$promo/$weekNumber")
            .get()
            .addOnSuccessListener { response ->
                resultBuilder[weekNumber] = mutableListOf()
                response.documents.forEach { document ->
                    resultBuilder[weekNumber]!!.add(document.toCourseV2())
                    Timber.i("mainPage: ${document.data}")
                }
                Timber.i("mainPage: $resultBuilder")
                result.value = resultBuilder
            }
            .addOnFailureListener { e ->
                Timber.i("mainPage: $e")
            }

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
            .set(
                mapOf("updated_at" to Timestamp(Date())),
                SetOptions.merge()
            ) // Used to create the document if not exists
            .addOnSuccessListener {
                db.collection("Types").document("data")
                    .update(
                        "Professors", FieldValue.arrayUnion(toUpperCase(course.professor!!)),
                        "Classrooms", FieldValue.arrayUnion(toUpperCase(course.classroom!!)),
                        "Subjects", FieldValue.arrayUnion(course.subject!!),
                        "Promotions", FieldValue.arrayUnion(toUpperCase(promo)),
                    )
                    .addOnSuccessListener {
                        db.collection("/Courses/$promo/$weekNumber")
                            .add(course)
                            .addOnSuccessListener {
                                resultLiveData.value = Result.Success("Course added successfully")
                                Timber.i("upload: SUCCESS")
                            }
                            .addOnFailureListener { e -> returnException(resultLiveData, e) }
                    }
                    .addOnFailureListener { e -> returnException(resultLiveData, e) }
            }
            .addOnFailureListener { e -> returnException(resultLiveData, e) }


    }

    fun testFunction() {

    }

    fun getPreFilledValues(): MutableLiveData<HashMap<String, MutableList<String>>> {
        val result = MutableLiveData(HashMap<String, MutableList<String>>())

        db.collection("Types").document("data")
            .get()
            .addOnSuccessListener { response ->
                val resultBuilder = HashMap<String, MutableList<String>>()
                Timber.i("${response.data}")
                response.data?.forEach { (key, value) ->
                    resultBuilder[key] =
                        (value as List<*>).filterIsInstance<String>().toMutableList()
                    resultBuilder[key]?.add(0, "+ Add $key")
                }
                result.value = resultBuilder
            }
            .addOnFailureListener {

            }

        return result
    }


    private fun returnException(resultLiveData: MutableLiveData<Result<String>>, e: Exception) {
        resultLiveData.value = Result.Error(e)
        Timber.i("upload: FAILURE: $e")
    }

}