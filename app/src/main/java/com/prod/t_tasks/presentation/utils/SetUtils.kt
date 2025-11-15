package com.prod.t_tasks.presentation.utils

fun <T> MutableSet<T>.toggleItem(item: T) {
	if (item in this) {
		remove(item)
	} else {
		add(item)
	}
}