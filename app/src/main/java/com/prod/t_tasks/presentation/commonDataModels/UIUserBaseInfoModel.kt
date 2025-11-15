package com.prod.t_tasks.presentation.commonDataModels

import com.prod.t_tasks.domain.model.User

data class UIUserBaseInfoModel(
	val id: Long,
	val name: String,
	val email: String,
	val imageURL: String?
)


fun User.mapToUI(): UIUserBaseInfoModel = UIUserBaseInfoModel(
	id = id,
	name = "$firstName $secondName",
	email = email,
	imageURL = null
)