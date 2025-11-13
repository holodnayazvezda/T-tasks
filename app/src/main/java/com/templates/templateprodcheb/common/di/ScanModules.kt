package com.templates.templateprodcheb.common.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.templates.templateprodcheb.presentation")
class AppModule

@Module
@ComponentScan("com.templates.templateprodcheb.domain")
class DomainModule

@Module
@ComponentScan("com.templates.templateprodcheb.data")
class DataModule