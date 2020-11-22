package com.novyapp.superplanning.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.fromISOtoCalendar
import com.novyapp.superplanning.toISOString
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.*

object FirebaseDataSource {

    private var database = Firebase.database.reference
    private var db = Firebase.firestore


    fun getCoursesFromPromoByWeek(
        promo: String,
        week: Int = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
    ): MutableLiveData<MutableList<Course>> {
        val result = MutableLiveData(mutableListOf<Course>())
        Timber.i("Fetching data with promo = $promo and week = $week")

        database.child("courses")
            .child(promo)
            .child(week.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataFetched = mutableListOf<Course>()
                    for (course in snapshot.children) {
                        val newCourse = course.getValue(Course::class.java)
                        newCourse?.let { dataFetched.add(it) }
                    }
                    Timber.i(dataFetched.toString())
                    result.value = dataFetched
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.message)
                }
            })
        return result
    }

//    fun getAllCoursesByPromo(promo: String): MutableLiveData<MutableList<Course>> {
//        val result = MutableLiveData(mutableListOf<Course>())
//        Timber.i("Fetching All Courses with promo = $promo")
//
//        database.child("courses")
//                .child(promo)
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        Timber.i("Snapshot: $snapshot")
//                        val dataFetched = mutableListOf<Course>()
//                        snapshot.children.forEach { dataSnapshot ->
//                            dataSnapshot.children.forEach { course ->
//                                val newCourse = course.getValue(Course::class.java)
//                                newCourse?.let { dataFetched.add(it) }
//                            }
//                        }
//                        Timber.i(dataFetched.toString())
//                        result.value = dataFetched
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Timber.e(error.message)
//                    }
//                })
//        return result
//    }

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

    fun addNewCourseToPromo(
        promo: String,
        subject: String,
        teacher: String,
        date: Calendar,
        classroom: String
    ) {
        database.child("courses")
            .child(promo)
            .child(date.get(Calendar.WEEK_OF_YEAR).toString())
            .push()
            .setValue(Course(subject, teacher, date.toISOString(), classroom))
    }

    fun addNewCourseToPromo(
        promo: String,
        subject: String,
        teacher: String,
        dateISO: String,
        classroom: String
    ) {
        val date = dateISO.fromISOtoCalendar()
        database.child("courses")
            .child(promo)
            .child(date.get(Calendar.WEEK_OF_YEAR).toString())
            .push()
            .setValue(Course(subject, teacher, date.toISOString(), classroom))
    }

    fun addCourseToPromo(course: CourseV2, promo: String = "B1-INFO-2020-2021"): MutableLiveData<String> {
        Timber.i("upload: Firebase data source launched")
        val result = MutableLiveData<String>()
        course.date?.let {
            Timber.i("upload: timestamp not null")
            val cal = Calendar.getInstance().also { it.timeInMillis = course.date.seconds*1000 }
            val weekNumber = cal.get(Calendar.WEEK_OF_YEAR)
            Timber.i("upload: weekNumber = $weekNumber")
            db.collection("/Courses/$promo/Weeks/$weekNumber/planning")
                .add(course)
                .addOnSuccessListener {
                    result.value = "Course added successfully"
                    Timber.i("upload: SUCCESS")
                }
                .addOnFailureListener { e ->
                    result.value = e.message
                    Timber.i("upload: FAILURE")
                }
        } ?: Timber.i("upload: timestamp IS null")
        Timber.i("upload: Value returned")
        return result
    }

//    fun updateCourse(promo: String, courseId: String, data: Course) {
//        database.child("courses").child(promo).child(courseId).setValue(data)
//    }


}