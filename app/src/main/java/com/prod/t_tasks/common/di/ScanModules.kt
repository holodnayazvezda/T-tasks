package com.prod.t_tasks.common.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.prod.team_sixteen.presentation")
class AppModule

@Module
@ComponentScan("com.prod.team_sixteen.domain")
class DomainModule

@Module
@ComponentScan("com.prod.team_sixteen.data")
class DataModule