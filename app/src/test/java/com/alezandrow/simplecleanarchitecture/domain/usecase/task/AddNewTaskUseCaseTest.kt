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

class AddNewTaskUseCaseTest {

    private lateinit var repository: TaskRepository
    private lateinit var useCase: AddNewTaskUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddNewTaskUseCase(repository)
    }

    @Test
    fun `invoke should return success when add new task succeeds`() = runTest {
        val task = mockk<Task>()
        val expected = AppResult.Success(Unit)

        coEvery { repository.addNewTask(task) } returns expected
        val result = useCase(task)

        assertEquals(expected, result)
        coVerify(exactly = 1) {
            repository.addNewTask(task)
        }
    }

    @Test
    fun `invoke should return error when add new task fails`() = runTest {
        val task = mockk<Task>()
        val expected = AppResult.Error(AppError.Unknown("Failed to add new task"))

        coEvery { repository.addNewTask(task) } returns expected
        val result = useCase(task)

        assertEquals(expected, result)
        coVerify(exactly = 1) {
            repository.addNewTask(task)
        }
    }
}