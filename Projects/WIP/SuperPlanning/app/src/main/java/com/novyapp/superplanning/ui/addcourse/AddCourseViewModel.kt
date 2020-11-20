package com.novyapp.superplanning.ui.addcourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddCourseViewModel : ViewModel() {
    var subject: String = ""
    var professor: String = ""
    var promotion: String = ""
    var classroom: String = ""
    var date: String = ""


}
class AddCourseViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(AddCourseViewModel::class.java))
            return AddCourseViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}
