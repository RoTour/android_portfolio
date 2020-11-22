package com.novyapp.superplanning.ui.addcourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.data.FirebaseDataSource
import timber.log.Timber
import java.util.*

class AddCourseViewModel : ViewModel() {

    var subject: String = ""
    var professor: String = ""
    var promotion: String = ""
    var classroom: String = ""
    var day: Calendar? = null
    var time: Date? = null

    var uploadResult: MutableLiveData<String> = MutableLiveData()

    fun saveNewCourse() {
        if (checkInputsEmpty()){
            Timber.i("upload: save aborted on field is empty")
            uploadResult.value = "Please fill every field."
            return
        }
        Timber.i("upload: day = $day")
        Timber.i("upload: time = $time")
        val timestamp = Timestamp(time!!.time/1000,0)

        Timber.i("upload: timestamp = ${timestamp.toDate()}")
        val newCourse = CourseV2(timestamp, professor, subject, classroom)
        uploadResult = FirebaseDataSource.addCourseToPromo(newCourse)
    }

    private fun checkInputsEmpty(): Boolean {
        return (subject.isEmpty() || professor.isEmpty() || promotion.isEmpty() ||
                classroom.isEmpty() || day == null || time == null)
    }
}

class AddCourseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java))
            return AddCourseViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}
