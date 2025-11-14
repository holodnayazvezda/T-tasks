package com.prod.t_tasks.data.data_providers.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
	@PrimaryKey
	val id: Long,
	@ColumnInfo(name = "first_name")
	val firstName: String,
	@ColumnInfo(name = "second_name")
	val secondName: String,
	val email: String,
	@ColumnInfo(name = "avatar_file_key")
	val avatarFileKey: String?
)
