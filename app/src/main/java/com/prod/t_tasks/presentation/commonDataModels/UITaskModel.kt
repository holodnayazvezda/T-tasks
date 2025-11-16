package com.prod.t_tasks.presentation.commonDataModels

import androidx.annotation.DrawableRes
import com.prod.t_tasks.R

import com.prod.t_tasks.domain.model.Task

data class UITaskModel(
	val id: Long,
	val name: String,
	@DrawableRes
	val typeIcon: Int,
	val deadline: Long,
	val archived: Boolean,
	val common: Boolean
)

fun Task.mapToUI(): UITaskModel = UITaskModel(
	id = id,
	name = name,
	typeIcon = getTypeIcon(typeIds.let { if (it.size == 1) it[0] else -1 }),
	deadline = deadline,
	archived = archived,
	common = common
)

fun getTypeIcon(id: Int): Int {
	return R.drawable.ic_documents
}