package com.novyapp.test.fasttypingtraining.ui.game

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.novyapp.test.fasttypingtraining.R
import com.novyapp.test.fasttypingtraining.ResourcesProvider
import com.novyapp.test.fasttypingtraining.ServiceLocator
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.domain.WordsSetup
import com.novyapp.test.fasttypingtraining.data.source.FakeAndroidTestRepository
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class GameFragmentTest {

    private lateinit var repository: WordsRepositoryInterface

    @Before
    fun initRepository() {

//        repository = FakeAndroidTestRepository(mutableListOf(current, next))
        repository = FakeAndroidTestRepository()
        ServiceLocator.wordsRepository = repository
        ResourcesProvider.context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun cleanupRepository() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun currentAndNextWords_displayedInUi() = runBlockingTest {
        val current = DomainWord(1L, "Temmie")
        val next = DomainWord(2L, "Flakes")
        val plus = DomainWord(3L, "Breakfast")
        repository.saveWords(plus)
        repository.wordsSetup = MutableLiveData(
            WordsSetup(
                current = MutableLiveData(current),
                next = MutableLiveData(next),
                preloadedList = mutableListOf(plus)
            )
        )
        launchFragmentInContainer<GameFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.currentWord_textView)).check(matches(withText(current.word)))
        onView(withId(R.id.nextWord_textView)).check(matches(withText(next.word)))
        onView(withId(R.id.game_editText)).perform(replaceText("${current.word} "))
        onView(withId(R.id.currentWord_textView)).check(matches(withText(next.word)))
        onView(withId(R.id.nextWord_textView)).check(matches(withText(plus.word)))
    }

    @Test
    fun textColorIsUpdatedInUi_whenCorrectAndIncorrect() = runBlockingTest {
        // Given

        val current = DomainWord(1L, "Temmie")
        val next = DomainWord(2L, "Flakes")
        val plus = DomainWord(3L, "Breakfast")
        repository.saveWords(plus)
        repository.wordsSetup = MutableLiveData(
            WordsSetup(
                current = MutableLiveData(current),
                next = MutableLiveData(next),
                preloadedList = mutableListOf(plus)
            )
        )
        launchFragmentInContainer<GameFragment>(themeResId = R.style.AppTheme)

        // When
        onView(withId(R.id.game_editText))
            .perform(replaceText("Temmy"))
        // Then
            .check(matches(hasTextColor(R.color.errorTextColor)))
        // When
            .perform(replaceText("Temm"))
        // Then
            .check(matches(hasTextColor(R.color.primaryTextColor)))
    }
    
    @Test
    fun navigateToResult_whenGameIsClosed() = runBlockingTest{
        // Given
    
        val current = DomainWord(1L, "Temmie")
        val next = DomainWord(2L, "Flakes")
        val plus = DomainWord(3L, "Breakfast")
        repository.saveWords(plus)
        repository.wordsSetup = MutableLiveData(
            WordsSetup(
                current = MutableLiveData(current),
                next = MutableLiveData(next),
                preloadedList = mutableListOf(plus)
            )
        )
        val scenario = launchFragmentInContainer<GameFragment>(themeResId = R.style.AppTheme)
        val navController = mock(NavController::class.java)
        // When
        onView(withId(R.id.game_editText)).perform(replaceText("a"))
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
            it.viewModel.closeGameFromTest()
        }
        // Then
        verify(navController).navigate(
            GameFragmentDirections.actionGameFragmentToResultsFragment(0, COUNTDOWN_TIME)
        )
    }

}