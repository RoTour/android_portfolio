package com.novyapp.superplanning.data

import java.util.Calendar
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.fromISOtoCalendar
import com.novyapp.superplanning.toISOString
import timber.log.Timber

object FirebaseDataSource {

    private var database = Firebase.database.reference


    fun getCoursesFromPromo(
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

    fun updateCourse(promo: String, courseId: String, data: Course) {
        database.child("courses").child(promo).child(courseId).setValue(data)
    }

    fun testPushList(promo: String) {
        database.child("courses").child(promo).push().setValue(listOf(
                Course("test1", "huon", "2020-10-26T08:30:00.000Z", "TOU-003"),
                Course("test2", "huon", "2020-10-26T08:30:00.000Z", "TOU-003"),
                Course("test3", "huon", "2020-10-26T08:30:00.000Z", "TOU-003")
        ))
    }

}