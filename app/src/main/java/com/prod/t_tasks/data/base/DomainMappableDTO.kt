package com.prod.t_tasks.data.base

interface DomainMappableDTO <T> {
	fun mapToDomain(): T
}