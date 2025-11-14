package com.prod.t_tasks.data.base

interface DBMappableDTO <T> {
	fun mapToDB(): T
}