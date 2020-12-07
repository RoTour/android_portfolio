package com.novyapp.superplanning.ui.addcourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.data.FirebaseDataSource
import com.novyapp.superplanning.data.Result
import java.util.*

enum class DataTypes(val value: String) {
    SUBJECT("Subjects"),
    PROFESSOR("Professors"),
    PROMOTION("Promotions"),
    CLASSROOM("Classrooms")
}


class AddCourseViewModel : ViewModel() {

    private var subject: String = ""
    private var professor: String = ""
    private var promotion: String = ""
    private var classroom: String = ""
    private var day: Calendar? = null
    private var time: Date? = null

    var spinnersData: MutableLiveData<HashMap<String, MutableList<String>>> =
        FirebaseDataSource.getPreFilledValues()

    var uploadResult: MutableLiveData<Result<String>> = MutableLiveData()

    fun saveNewCourse() {
        if (checkInputsEmpty()) return
        val timestamp = Timestamp(time!!.time / 1000, 0)
        val newCourse = CourseV2(subject, professor, classroom, timestamp)
        FirebaseDataSource.addCourseToPromo(newCourse, promotion, uploadResult)
        uploadResult.value = Result.Loading()
    }

    private fun checkInputsEmpty(): Boolean {
        return if (subject.isEmpty() || professor.isEmpty() || promotion.isEmpty() || classroom.isEmpty() || day == null || time == null) {
            uploadResult.value = Result.Error(Exception("Error: A field isn't filled correctly"))
            true
        } else false
    }

    fun resetInputs() {
        professor = ""; promotion = ""; classroom = ""; day = null; time = null
    }

    fun newValueOn(dataType: String, newValue: String) {
        spinnersData.value?.get(dataType)?.add(newValue)
    }


    /**
     * Getters / Setters (only needed ones)
     */
    fun subject(newSubject: String) { subject = newSubject }
    fun professor(newProfessor: String) { professor = newProfessor }
    fun promotion(newPromotion: String) { promotion = newPromotion }
    fun classroom(newClassroom: String) { classroom = newClassroom }
    fun day(newDay: Calendar) { day = newDay }
    fun day(): Calendar{ return day ?: Calendar.getInstance() }
    fun time(newTime: Date) { time = newTime }
    fun time(): Date{ return time ?: Date() }
}

class AddCourseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java))
            return AddCourseViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}
