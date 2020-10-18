package com.novyapp.test.fasttypingtraining.ui.results

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.novyapp.test.fasttypingtraining.R
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class ResultsFragmentTest {

    @Test
    fun wordsPerMinute_displayedInUi() {
        // Given
        val score = 10
        val countdownMilli = 10000L

        // When
        val bundle = ResultsFragmentArgs(score, countdownMilli).toBundle()
        launchFragmentInContainer<ResultsFragment>(bundle, R.style.AppTheme)

        // Then
        onView(withId(R.id.wordsPerMinutes_textView)).check(matches(withText("60.0")))
    }

}