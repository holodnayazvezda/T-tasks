import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.googleKsp)
	alias(libs.plugins.room)
	alias(libs.plugins.serialization)
	alias(libs.plugins.secrets.gradle.plugin)
	alias(libs.plugins.kotlin.parcelize)
	alias(libs.plugins.google.services.gmc)
	alias(libs.plugins.firebase.crashlytics)
}
android {
	namespace = "com.prod.t_tasks"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.prod.t_tasks"
		minSdk = 29
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	room {
		schemaDirectory("$projectDir/schemas")
	}


	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}

	buildFeatures {
		compose = true
		buildConfig = true
		viewBinding = true
	}

	ksp {
		arg("KOIN_CONFIG_CHECK", "true")
		arg("KOIN_DEFAULT_MODULE", "false")
		arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
	}
	composeOptions.kotlinCompilerExtensionVersion = "1.5.6"
	packagingOptions.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
	kotlin {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_21)
			freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
		}
	}
}

dependencies {
	// Base libraries ------------------------------------------------------------------------------

	implementation(libs.core.ktx)
	implementation(platform(libs.kotlin.bom))
	implementation(libs.lifecycle.runtime.ktx)
	implementation(libs.activity.compose)
	implementation(platform(libs.compose.bom))
	implementation(libs.ui)
	implementation(libs.ui.graphics)
	implementation(libs.ui.tooling.preview)
	implementation(libs.compose.animation.graphics)
	implementation(libs.material.icons.extended)
    implementation(libs.androidx.foundation)
    androidTestImplementation(platform(libs.compose.bom))
	androidTestImplementation(libs.androidx.ui.test.junit4)
	debugImplementation(libs.ui.tooling)
	debugImplementation(libs.ui.test.manifest)

	// Material
	implementation(libs.material3)

	// Navigation
	implementation(libs.navigation.compose)

	// Data store
	implementation(libs.androidx.datastore.preferences)

	// TESTING -------------------------------------------------------------------------------------

	testImplementation(libs.junit)
	androidTestImplementation(libs.ext.junit)
	androidTestImplementation(libs.espresso.core)

	// Koin testing
	testImplementation(libs.koin.test)

	// IMAGES --------------------------------------------------------------------------------------

	// Coil
	implementation(libs.coil.compose)
	implementation(libs.coil.svg)
	implementation(libs.coil.gif)

	// VIEW ELEMENTS -------------------------------------------------------------------------------

	// Lottie animation for start
	implementation(libs.lottie.compose)

	// Fonts
	implementation(libs.ui.text.google.fonts)

	// Constraint layout
	implementation(libs.constraintlayout.compose)

	// NETWORK -------------------------------------------------------------------------------------

	// gson
	implementation(libs.gson)


	// Ktor
	implementation(libs.ktor.serialization.kotlinx.json)
	implementation(libs.ktor.client.logging)
	implementation(libs.ktor.client.core)
	implementation(libs.io.ktor.ktor.client.cio)
	implementation(libs.ktor.client.websockets)
	implementation(libs.ktor.client.negotiation)
	implementation(libs.ktor.client.auth)

	// Paging
	implementation(libs.androidx.paging.runtime.ktx)
	implementation(libs.androidx.paging.compose)

	// GOOGLE AND FACEBOOK SERVICES ----------------------------------------------------------------

	// Cloud messaging
	implementation(libs.play.services.gcm)
	implementation(libs.google.firebase.analytics)
	implementation(libs.firebase.messaging)

	// Import the BoM for the Firebase platform
	implementation(platform(libs.firebase.bom))

	// Guava
	implementation(libs.guava)

	// OTHER SERVICES ------------------------------------------------------------------------------

	// CameraX
	implementation(libs.camera.core)
	implementation(libs.camera.camera2)
	implementation(libs.androidx.camera.lifecycle)
	implementation(libs.androidx.camera.video)
	implementation(libs.androidx.camera.view)
	implementation(libs.androidx.camera.extensions)

	// Koin
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.koin.annotations)
	implementation(libs.koin.androidx.compose)
	implementation(libs.koin.androidx.compose.navigation)
	ksp(libs.koin.ksp.compiler)
	ksp(libs.koin.ksp.bom)

	// Viewmodel
	implementation(libs.androidx.savedstate.ktx)
	implementation(libs.androidx.lifecycle.viewmodel.ktx)
	implementation(libs.androidx.lifecycle.service)
	implementation(libs.lifecycle.runtime.ktx)

	// Room
	implementation(libs.androidx.room.runtime)
	ksp(libs.androidx.room.compiler)
	implementation(libs.androidx.room.ktx)
	implementation(libs.androidx.room.paging)

	// Permission manager
	implementation(libs.accompanist.permissions)

	// System UI controller
	implementation(libs.accompanist.systemuicontroller)

	implementation(libs.androidx.core.splashscreen)

	// END -------------------------------------------------------------------------------------
}
