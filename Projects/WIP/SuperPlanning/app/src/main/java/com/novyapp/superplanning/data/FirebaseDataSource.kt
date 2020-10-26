package com.novyapp.superplanning.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object FirebaseDataSource {

    private var database = Firebase.database.reference

    fun getCoursesFromPromo(promo: String): MutableLiveData<MutableList<Course>> {
        val result = MutableLiveData(mutableListOf<Course>())

        database.child("courses").child(promo).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataFetched = mutableListOf<Course>()
                for (course in snapshot.children){
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

    fun addNewCourseToPromo(promo: String, subject: String, teacher: String, date: String, classroom: String){
        database.child("courses").child(promo).push().setValue(Course(subject, teacher, date, classroom))
    }

    fun updateCourse(promo: String, courseId: String, data: Course){
        database.child("courses").child(promo).child(courseId).setValue(data)
    }

}