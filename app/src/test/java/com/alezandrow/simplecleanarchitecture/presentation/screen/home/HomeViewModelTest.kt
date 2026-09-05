package com.alezandrow.simplecleanarchitecture.presentation.screen.home

import android.content.Context
import androidx.credentials.CredentialManager
import app.cash.turbine.test
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignOutUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.task.GetTasksByTitleAndPriorityUseCase
import com.alezandrow.simplecleanarchitecture.presentation.MainDispatcherRule
import com.alezandrow.simplecleanarchitecture.presentation.state.TaskListUiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var signOutUseCase: SignOutUseCase
    private lateinit var getTasksByTitleAndPriority: GetTasksByTitleAndPriorityUseCase
    private lateinit var credentialManager: CredentialManager
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        signOutUseCase = mockk()
        getTasksByTitleAndPriority = mockk()
        credentialManager = mockk()

        every {
            getTasksByTitleAndPriority(any(), any())
        } returns flowOf(AppResult.Success(emptyList()))

        viewModel = HomeViewModel(
            signOutUseCase = signOutUseCase,
            getTasksByTitleAndPriority = getTasksByTitleAndPriority,
            credentialManager = credentialManager
        )
    }

    @Test
    fun `searchQuery should have empty initial value`() = runTest {
        assertEquals("", viewModel.searchQuery.value)
    }

    @Test
    fun `selectedPriority should have null initial value`() = runTest {
        assertNull(viewModel.selectedPriority.value)
    }

    @Test
    fun `updateSearchQuery should update search query`() = runTest {
        viewModel.updateSearchQuery("Kotlin")
        assertEquals("Kotlin", viewModel.searchQuery.value)
    }

    @Test
    fun `setFilterPriority should set priority when priority is different`() = runTest {
        viewModel.setFilterPriority(TaskPriority.HIGH)
        assertEquals(TaskPriority.HIGH, viewModel.selectedPriority.value)
    }

    @Test
    fun `setFilterPriority should clear priority when same priority is selected`() = runTest {
        viewModel.setFilterPriority(TaskPriority.HIGH)
        viewModel.setFilterPriority(TaskPriority.HIGH)
        assertNull(viewModel.selectedPriority.value)
    }

    @Test
    fun `setFilterPriority should replace priority when different priority is selected`() =
        runTest {
            viewModel.setFilterPriority(TaskPriority.HIGH)
            viewModel.setFilterPriority(TaskPriority.LOW)
            assertEquals(TaskPriority.LOW, viewModel.selectedPriority.value)
        }

    @Test
    fun `taskListUiState should initially emit loading`() = runTest {
        viewModel.taskListUiState.test {
            assertEquals(TaskListUiState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `taskListUiState should emit success when tasks are loaded`() = runTest {
        val tasks = listOf(mockk<Task>(), mockk<Task>())

        every {
            getTasksByTitleAndPriority("", null)
        } returns flowOf(AppResult.Success(tasks))

        viewModel.taskListUiState.test {
            assertEquals(
                TaskListUiState.Loading, awaitItem()
            )

            advanceTimeBy(301.milliseconds)

            assertEquals(TaskListUiState.Success(tasks), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("", null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `taskListUiState should emit error when loading tasks fails`() = runTest {
        val exception = Exception("Failed to load tasks")
        every {
            getTasksByTitleAndPriority(
                "",
                null
            )
        } returns flowOf(AppResult.Error(AppError.Unknown("Failed to load tasks")))

        viewModel.taskListUiState.test {
            assertEquals(TaskListUiState.Loading, awaitItem())
            advanceTimeBy(301.milliseconds)
            assertEquals(
                TaskListUiState.Error("Failed to load tasks"),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("", null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `taskListUiState should request tasks with updated search query`() = runTest {
        val tasks = listOf(mockk<Task>())

        every {
            getTasksByTitleAndPriority(
                "Kotlin",
                null
            )
        } returns flowOf(AppResult.Success(tasks))

        viewModel.taskListUiState.test {
            assertEquals(
                TaskListUiState.Loading,
                awaitItem()
            )
            advanceTimeBy(301.milliseconds)
            assertEquals(TaskListUiState.Success(emptyList()), awaitItem())
            viewModel.updateSearchQuery("Kotlin")
            advanceTimeBy(301.milliseconds)
            assertEquals(TaskListUiState.Success(tasks), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("Kotlin", null)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `taskListUiState should request tasks with selected priority`() = runTest {
        val tasks = listOf(mockk<Task>())

        every {
            getTasksByTitleAndPriority(
                "",
                TaskPriority.HIGH
            )
        } returns flowOf(AppResult.Success(tasks))

        viewModel.taskListUiState.test {
            assertEquals(
                TaskListUiState.Loading,
                awaitItem()
            )
            advanceTimeBy(301.milliseconds)
            assertEquals(TaskListUiState.Success(emptyList()), awaitItem())
            viewModel.setFilterPriority(TaskPriority.HIGH)
            runCurrent()
            assertEquals(TaskListUiState.Success(tasks), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("", TaskPriority.HIGH)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `taskListUiState should request tasks with search query and priority`() = runTest {
        val tasks = listOf(mockk<Task>())

        every {
            getTasksByTitleAndPriority("", null)
        } returns flowOf(
            AppResult.Success(emptyList())
        )

        every {
            getTasksByTitleAndPriority("Kotlin", null)
        } returns flowOf(
            AppResult.Success(tasks)
        )

        every {
            getTasksByTitleAndPriority("Kotlin", TaskPriority.HIGH)
        } returns flowOf(
            AppResult.Success(tasks)
        )

        viewModel.taskListUiState.test {
            assertEquals(
                TaskListUiState.Loading,
                awaitItem()
            )

            runCurrent()
            advanceTimeBy(300.milliseconds)
            runCurrent()

            assertEquals(
                TaskListUiState.Success(emptyList()),
                awaitItem()
            )

            viewModel.updateSearchQuery("Kotlin")

            advanceTimeBy(300)
            runCurrent()

            assertEquals(
                TaskListUiState.Success(tasks),
                awaitItem()
            )

            viewModel.setFilterPriority(TaskPriority.HIGH)

            runCurrent()

            cancelAndIgnoreRemainingEvents()
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("", null)
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("Kotlin", null)
        }

        verify(exactly = 1) {
            getTasksByTitleAndPriority("Kotlin", TaskPriority.HIGH)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `signOut should invoke signOutUseCase`() = runTest {

        coEvery { signOutUseCase() } returns AppResult.Success(Unit)
        val context = mockk<Context>(relaxed = true)

        viewModel.signOut(context)

        advanceUntilIdle()

        coVerify(exactly = 1) {
            signOutUseCase()
        }
    }


}