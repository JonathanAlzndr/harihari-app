package com.alezandrow.simplecleanarchitecture.di

import com.alezandrow.simplecleanarchitecture.data.repository.TaskRepositoryImpl
import com.alezandrow.simplecleanarchitecture.domain.repository.ITaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class DomainModule {

    @Binds
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): ITaskRepository
}