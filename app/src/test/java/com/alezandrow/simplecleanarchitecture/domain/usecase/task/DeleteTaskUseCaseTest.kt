package com.alezandrow.simplecleanarchitecture.domain.usecase.task

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DeleteTaskUseCaseTest {

    private lateinit var repository: TaskRepository
    private lateinit var useCase: DeleteTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteTaskUseCase(repository)
    }

    @Test
    fun `invoke should return success when delete task succeeds`() = runTest {
        val taskId = "task-123"
        val expected = AppResult.Success(Unit)

        coEvery {
            repository.deleteTask(taskId)
        } returns expected

        val result = useCase(taskId)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            repository.deleteTask(taskId)
        }
    }

    @Test
    fun `invoke should return error when delete task fails`() = runTest {
        val taskId = "task-123"
        val expected = AppResult.Error(AppError.Unknown("Failed to delete task"))

        coEvery {
            repository.deleteTask(taskId)
        } returns expected

        val result = useCase(taskId)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            repository.deleteTask(taskId)
        }
    }
}