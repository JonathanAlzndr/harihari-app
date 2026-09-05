package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import app.cash.turbine.test
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.entities.task.TaskPriority
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTasksByTitleAndPriorityUseCaseTest {
    private lateinit var taskRepository: TaskRepository
    private lateinit var useCase: GetTasksByTitleAndPriorityUseCase

    @Before
    fun setup() {
        taskRepository = mockk()
        useCase = GetTasksByTitleAndPriorityUseCase(taskRepository)
    }

    @Test
    fun `invoke should emit tasks when repository returns success`() = runTest {
        val title = "Learn Kotlin"
        val priority = TaskPriority.HIGH
        val tasks = listOf(mockk<Task>(), mockk<Task>())
        val expected = AppResult.Success(tasks)

        every {
            taskRepository.getTasksByTitleAndPriority(
                title,
                priority
            )
        } returns flowOf(expected)


        useCase(title, priority).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { taskRepository.getTasksByTitleAndPriority(title, priority) }
    }

    @Test
    fun `invoke should emit error when repository returns error`() = runTest {
        val title = "Learn Kotlin"
        val priority = TaskPriority.HIGH
        val expected = AppResult.Error(AppError.Unknown("Failed to get tasks"))

        every {
            taskRepository.getTasksByTitleAndPriority(title, priority)
        } returns flowOf(expected)

        useCase(title, priority).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) { taskRepository.getTasksByTitleAndPriority(title, priority) }
    }

    @Test
    fun `invoke should pass null priority to repository`() = runTest {
        val title = "Learn Kotlin"
        val expected = AppResult.Success(emptyList<Task>())
        every {
            taskRepository.getTasksByTitleAndPriority(
                title,
                null
            )
        } returns flowOf(expected)
        useCase(title, null).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) {
            taskRepository.getTasksByTitleAndPriority(title, null)
        }
    }

    @Test
    fun `invoke should emit multiple task results`() = runTest {
        val title = "Learn Kotlin"
        val priority = TaskPriority.HIGH
        val tasks1 = listOf(mockk<Task>())
        val tasks2 = listOf(mockk<Task>(), mockk<Task>())
        val expected1 = AppResult.Success(tasks1)
        val expected2 = AppResult.Success(tasks2)

        every {
            taskRepository.getTasksByTitleAndPriority(
                title,
                priority
            )
        } returns flowOf(expected1, expected2)

        useCase(title, priority).test {
            assertEquals(expected1, awaitItem())
            assertEquals(expected2, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) {
            taskRepository.getTasksByTitleAndPriority(title, priority)
        }
    }
}
