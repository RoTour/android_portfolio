package com.novyapp.superplanning.ui.addcourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.data.FirebaseDataSource
import com.novyapp.superplanning.data.Result
import timber.log.Timber
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

enum class DataTypes(val value: String) {
    SUBJECT("Subjects"),
    PROFESSOR("Professors"),
    PROMOTION("Promotions"),
    CLASSROOM("Classrooms")
}


class AddCourseViewModel : ViewModel() {

    var subject: String = ""
    var professor: String = ""
    var promotion: String = ""
    var classroom: String = ""
    var day: Calendar? = null
    var time: Date? = null

    var spinnersData: MutableLiveData<Map<String, Any>> = FirebaseDataSource.getDistinctSubjects()

//    : MutableLiveData<HashMap<String, MutableList<String>>>
    var displayData = Transformations.map(spinnersData) {
        val result = HashMap<String, MutableList<String>>()
        it.forEach { (key, value) -> result[key] = (value as List<*>).filterIsInstance<String>().toMutableList() }
        result
    }

    var uploadResult: MutableLiveData<Result<String>> = MutableLiveData()

    fun saveNewCourse() {
        if(checkInputsEmpty()) return

        val timestamp = Timestamp(time!!.time/1000,0)
        val newCourse = CourseV2(timestamp, professor, subject, classroom)
        Timber.i("upload: timestamp = ${timestamp.toDate()}")
        FirebaseDataSource.addCourseToPromo(newCourse, promotion, uploadResult)
        uploadResult.value = Result.Loading()
    }

    fun checkInputsEmpty(): Boolean {
        return if (subject.isEmpty() || professor.isEmpty() || promotion.isEmpty() || classroom.isEmpty() || day == null || time == null) {
            Timber.i("upload: save aborted because at least a field is empty.")
            uploadResult.value = Result.Error(Exception("Error: A field isn't filled correctly"))
            true
        } else false
    }

    fun resetInputs() {
        professor = ""; promotion =""; classroom = ""; day = null; time = null
    }

    fun setNewSubject(newValue: String){
        subject = newValue
    }

    fun newValueOn(dataType: String, newValue: String) {
        val data = displayData.value
        data?.get(dataType)?.add(newValue)
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
