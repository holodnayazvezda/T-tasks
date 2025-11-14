// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.google.services.gmc) apply false
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.jetbrainsKotlinAndroid) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.googleKsp) apply false
	alias(libs.plugins.room) apply false
	alias(libs.plugins.serialization) apply false
}

buildscript {
	dependencies {
		classpath(libs.secrets.gradle.plugin)
		classpath(libs.build.tools)
		classpath (libs.google.services.gmc)
	}
}
