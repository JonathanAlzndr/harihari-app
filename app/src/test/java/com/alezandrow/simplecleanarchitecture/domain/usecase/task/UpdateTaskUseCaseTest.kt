package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.task.Task
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateTaskUseCaseTest {

    private lateinit var repository: TaskRepository
    private lateinit var useCase: UpdateTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpdateTaskUseCase(repository)
    }

    @Test
    fun `invoke should return success when update task succeeds`() = runTest {
        val task = mockk<Task>()
        val expected = AppResult.Success(Unit)

        coEvery {
            repository.updateTask(task)
        } returns expected

        val result = useCase(task)

        assertEquals(expected, result)

        coVerify(exactly = 1) { repository.updateTask(task) }
    }

    @Test
    fun `invoke should return error when update task fails`() = runTest {
        val task = mockk<Task>()
        val expected = AppResult.Error(AppError.Unknown("Failed to update task"))

        coEvery {
            repository.updateTask(task)
        } returns expected

        val result = useCase(task)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            repository.updateTask(task)
        }
    }
}