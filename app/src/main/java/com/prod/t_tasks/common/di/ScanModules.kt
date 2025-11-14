package com.prod.t_tasks.common.di

import com.prod.t_tasks.data.data_providers.local_db.DatabaseProvider
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class])
@ComponentScan("com.prod.t_tasks.presentation")
class AppModule

@Module(includes = [DataModule::class])
@ComponentScan("com.prod.t_tasks.domain")
class DomainModule

@Module(includes = [DatabaseProvider::class])
@ComponentScan("com.prod.t_tasks.data")
class DataModule
