package com.alezandrow.simplecleanarchitecture.util

import com.alezandrow.simplecleanarchitecture.common.AppError
import org.junit.Assert.assertEquals
import org.junit.Test

class AppErrorMapperTest {

    @Test
    fun `Network error should return Connection Issue`() {

        val error = AppError.Network

        val result = mapAppErrorToMessage(error)

        assertEquals("Connection issue", result)
    }

    @Test
    fun `InvalidCredentials error should return email or password wrong`() {

        val error = AppError.InvalidCredentials

        val result = mapAppErrorToMessage(error)

        assertEquals("Email or password wrong.", result)
    }

    @Test
    fun `UserNotFound error should return Account is not found`() {

        val error = AppError.UserNotFound

        val result = mapAppErrorToMessage(error)

        assertEquals("Account is not found", result)
    }

    @Test
    fun `AlreadyExists error should return Document Already Exist`() {

        val error = AppError.AlreadyExists

        val result = mapAppErrorToMessage(error)

        assertEquals("Document Already Exists", result)
    }

    @Test
    fun `Cancelled error should return Process Cancelled`() {

        val error = AppError.Cancelled

        val result = mapAppErrorToMessage(error)

        assertEquals("Process Cancelled", result)
    }

    @Test
    fun `PermissionDenied error should return Permission Denied`() {

        val error = AppError.PermissionDenied

        val result = mapAppErrorToMessage(error)

        assertEquals("Permission Denied", result)
    }

    @Test
    fun `Validation error should return its message`() {
        val error = AppError.Validation("Email is invalid")

        val result = mapAppErrorToMessage(error)

        assertEquals("Email is invalid", result)
    }

    @Test
    fun `Unknown error should return its message`() {

        val error = AppError.Unknown("Something went wrong")

        val result = mapAppErrorToMessage(error)

        assertEquals("Something went wrong", result)
    }

}