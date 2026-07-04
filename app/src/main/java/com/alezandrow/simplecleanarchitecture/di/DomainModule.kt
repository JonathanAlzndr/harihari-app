package com.alezandrow.simplecleanarchitecture.di

import com.alezandrow.simplecleanarchitecture.data.repository.AuthRepositoryImpl
import com.alezandrow.simplecleanarchitecture.data.repository.TaskRepositoryFirestoreImpl
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import com.alezandrow.simplecleanarchitecture.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryFirestoreImpl): TaskRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}