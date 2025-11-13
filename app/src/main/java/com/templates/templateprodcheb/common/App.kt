package com.templates.templateprodcheb.common

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.templates.templateprodcheb.common.di.AppModule
import com.templates.templateprodcheb.common.di.DataModule
import com.templates.templateprodcheb.common.di.DomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class App : Application() {

	companion object {
		lateinit var instance: Application
		lateinit var version: String
	}

	override fun onCreate() {
		super.onCreate()
		instance = this
		version = getAppVersion()
		startKoin {
			androidContext(this@App)
			modules(
				AppModule().module,
				DataModule().module,
				DomainModule().module
			)
		}
	}

	private fun getAppVersion(): String {
		var pInfo: PackageInfo? = null
		try {
			val pm = packageManager
			if (pm != null) {
				pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					pm.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
				} else {
					pm.getPackageInfo(packageName, 0)
				}
			}
		} catch (e: Exception) {
			Log.d("App", "method: getAppVersion - error: $e")
		}
		if (pInfo == null) {
			pInfo = PackageInfo()
			pInfo.versionName = "0.0.0"
			pInfo.longVersionCode = 0
		}
		var version = pInfo.versionName + "."
		version += pInfo.longVersionCode
		return version
	}
}