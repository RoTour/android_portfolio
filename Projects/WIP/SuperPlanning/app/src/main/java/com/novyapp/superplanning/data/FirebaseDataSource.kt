package com.novyapp.superplanning.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.fromISOtoCalendar
import com.novyapp.superplanning.toISOString
import timber.log.Timber
import java.util.*

object FirebaseDataSource {

    private var database = Firebase.database.reference


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
     */

    fun getAllCoursesByPromo(promo: String): MutableLiveData<LinkedHashMap<String, MutableList<Course>>>{
        val result = MutableLiveData(linkedMapOf<String, MutableList<Course>>())

        database.child("courses")
                .child(promo)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val toReturn = linkedMapOf<String, MutableList<Course>>()
                        snapshot.children.forEach { week ->
                            toReturn[week.key as String] = mutableListOf()
                            week.children.forEach { course ->
                                toReturn[week.key as String]!!.add(course.getValue(Course::class.java) ?: Course())
                            }
                        }
                        Timber.i("Fetched data: $toReturn")
                        result.value = toReturn
                    }
                    override fun onCancelled(error: DatabaseError) { Timber.i(error.message) }
                })
        return result
    }

    fun addNewCourseToPromo(promo: String, subject: String, teacher: String, date: Calendar, classroom: String) {
        database.child("courses")
                .child(promo)
                .child(date.get(Calendar.WEEK_OF_YEAR).toString())
                .push()
                .setValue(Course(subject, teacher, date.toISOString(), classroom))
    }

    fun addNewCourseToPromo(promo: String, subject: String, teacher: String, dateISO: String, classroom: String) {
        val date = dateISO.fromISOtoCalendar()
        database.child("courses")
                .child(promo)
                .child(date.get(Calendar.WEEK_OF_YEAR).toString())
                .push()
                .setValue(Course(subject, teacher, date.toISOString(), classroom))
    }

//    fun updateCourse(promo: String, courseId: String, data: Course) {
//        database.child("courses").child(promo).child(courseId).setValue(data)
//    }


}