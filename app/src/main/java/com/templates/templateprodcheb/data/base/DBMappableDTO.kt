package com.templates.templateprodcheb.data.base

interface DBMappableDTO <T> {
	fun mapToDB(): T
}