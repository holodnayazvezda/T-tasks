package com.templates.templateprodcheb.data.base

interface DomainMappableDTO <T> {
	fun mapToDomain(): T
}