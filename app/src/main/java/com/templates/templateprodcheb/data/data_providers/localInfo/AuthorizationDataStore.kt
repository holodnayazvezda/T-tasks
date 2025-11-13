package com.templates.templateprodcheb.data.data_providers.localInfo

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import org.koin.core.annotation.Single


@Single
class AuthorizationDataStore(
	context: Context
) {
	private val Context.dataStore by preferencesDataStore(
		name = "authTokens"
	)

	private val dataStore = context.dataStore
	private companion object {
		const val TAG = "AuthorizationDataStore"
		val ACCESS_TOKEN = stringPreferencesKey("accessToken")
		val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
	}

	suspend fun saveTokenPair(accessToken: String, refreshToken: String) {
		dataStore.edit { preferences ->
			preferences[ACCESS_TOKEN] = accessToken
			preferences[REFRESH_TOKEN] = refreshToken
		}
	}

	suspend fun clearTokenPair(){
		dataStore.edit { preferences ->
			preferences[ACCESS_TOKEN] = ""
			preferences[REFRESH_TOKEN] = ""
		}
	}

	val tokenPair
		get() = dataStore.data
			.catch {
				Log.e(TAG, "Error reading preferences.", it)
				if (it is IOException) {
					Log.e(TAG, "return empty prefs")
					emit(emptyPreferences())
				} else {
					throw it
				}
			}.map { preferences ->
				val accessToken = preferences[ACCESS_TOKEN]
				val refreshToken = preferences[REFRESH_TOKEN]

				Pair(
					accessToken ?: "",
					refreshToken ?: ""
				).also {
					Log.d(TAG, "Token pair: $it")
				}
			}
}