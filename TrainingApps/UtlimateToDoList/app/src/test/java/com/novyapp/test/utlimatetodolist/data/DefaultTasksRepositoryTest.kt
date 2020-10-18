package com.novyapp.test.utlimatetodolist.data

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.novyapp.test.utlimatetodolist.MainCoroutineRule
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.data.local.TaskLocalDataSource
import com.novyapp.test.utlimatetodolist.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DefaultTasksRepositoryTest {

    private val task1 = Task(taskTitle = "Title1", taskText = "Description1")
    private val task2 = Task(taskTitle = "Title2", taskText = "Description2")
    private val task3 = Task(taskTitle = "Title3", taskText = "Description3")

    private val originalLocalData = listOf(task1, task2)
    private val originalRemoteData = listOf(task1,task2, task3)


    private lateinit var taskLocalDataSource: IDataSource
    private lateinit var taskRemoteDataSource: IDataSource

    private lateinit var repository: DefaultTasksRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initRepository(){
        taskLocalDataSource = FakeDataSource(originalLocalData)
        taskRemoteDataSource = FakeDataSource(originalRemoteData)

        repository = DefaultTasksRepository(taskLocalDataSource, taskRemoteDataSource, Dispatchers.Main)
    }

    @Test
    fun getTask() = mainCoroutineRule.runBlockingTest {
        val result = repository.getTask(task1.id)
        assert(result is Result.Success)
        assertEquals(task1, (result as Result.Success).data)
    }

    @Test
    fun getTasks() = mainCoroutineRule.runBlockingTest{
        val result = repository.getTasks()
        assert(result is Result.Success)
        assertEquals(originalLocalData, (result as Result.Success).data)
    }

    @Test
    fun getObservableTask() = mainCoroutineRule.runBlockingTest {
        val result = repository.getObservableTask(task1.id)
        assertEquals(Result.Success(task1), result.getOrAwaitValue())
    }

    @Test
    fun getObservableTasks() = mainCoroutineRule.runBlockingTest {
        val result = repository.getObservableTasks()
        assertEquals(Result.Success(originalLocalData), result.getOrAwaitValue())
    }

    @Test
    fun saveTask() = mainCoroutineRule.runBlockingTest {
        repository.saveTask(task3)
        assertEquals(listOf(task1, task2, task3), (repository.getTasks() as Result.Success).data)
    }

    @Test
    fun saveTasks() = mainCoroutineRule.runBlockingTest {
        repository.saveTasks(task3, task2)
        assertEquals(listOf(task1, task2, task3), (repository.getTasks() as Result.Success).data)
    }

    @Test
    fun deleteTask() = mainCoroutineRule.runBlockingTest {
        repository.deleteTask(task1.id)
        assertThat(
            (repository.getTasks() as Result.Success).data,
            IsEqual(listOf(task2))
        )
    }

    @Test
    fun deleteTasks() = mainCoroutineRule.runBlockingTest {
        repository.deleteTasks()
        assertEquals(emptyList<Task>(), (repository.getTasks() as Result.Success).data)
    }

    @Test
    fun refreshFromRemote() = mainCoroutineRule.runBlockingTest{
        // Given
        // When
        repository.refreshFromRemote()
        val result = repository.getTasks()
        // Then
        assert(result is Result.Success)
        assertEquals(originalRemoteData, (result as Result.Success).data)
    }

    @Test
    fun toggleIsComplete() = mainCoroutineRule.runBlockingTest {
        repository.toggleIsComplete(task2)
        assertEquals(
            Task(task2.id, task2.taskTitle, task2.taskText, !task2.isCompleted),
            (repository.getTask(task2.id) as Result.Success).data
        )
    }

    @Test
    fun updateTask() = mainCoroutineRule.runBlockingTest {
        task2.isCompleted = !task2.isCompleted
        repository.updateTask(task2)
        assertEquals(
            Task(id = task2.id, taskTitle = "Title2", taskText = "Description2", isCompleted = true),
            (repository.getTask(task2.id) as Result.Success).data
        )
    }

}