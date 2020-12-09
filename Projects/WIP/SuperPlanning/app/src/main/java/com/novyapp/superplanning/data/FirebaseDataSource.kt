package com.novyapp.superplanning.data

import androidx.lifecycle.MutableLiveData
import com.google.common.base.Ascii.toUpperCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.todayWeekNumber
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

object FirebaseDataSource {

    private var db = Firebase.firestore


    /**
     * @param weekNumber to select from which week you want to get the data
     * @return : HashMap of WeekNumber => mutableListOfCoursesThisWeek
     */

    fun getCoursesByPromo(
        promo: String,
        weekNumber: String = todayWeekNumber().toString(),
        existingData: MutableLiveData<Result<HashMap<String, MutableList<CourseV2>>>>? = null
    ): MutableLiveData<Result<HashMap<String, MutableList<CourseV2>>>>? {

        val result =
            if (existingData != null) null
            else MutableLiveData<Result<HashMap<String, MutableList<CourseV2>>>>()

        db.collection("/Courses/$promo/$weekNumber")
            .get()
            .addOnSuccessListener { response ->
                val resultBuilder = (existingData?.value as Result.Success?)?.data ?: HashMap()
                resultBuilder[weekNumber] = mutableListOf()
                response.documents.forEach { document ->
                    resultBuilder[weekNumber]!!.add(document.toCourseV2())
                    Timber.i("mainPage: ${document.data}")
                }
                existingData?.let { it.value = Result.Success(resultBuilder) }
                    ?: run { result!!.value = Result.Success(resultBuilder) }
            }
            .addOnFailureListener { e ->
                Timber.i("mainPage: $e")
                result?.let { it.value = Result.Error(e) }
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
                            }
                            .addOnFailureListener { e -> returnException(resultLiveData, e) }
                    }
                    .addOnFailureListener { e -> returnException(resultLiveData, e) }
            }
            .addOnFailureListener { e -> returnException(resultLiveData, e) }


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
    }

}