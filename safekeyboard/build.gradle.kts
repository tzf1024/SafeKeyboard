plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("com.vanniktech.maven.publish") version "0.29.0"
	id("org.jetbrains.dokka") version "1.9.20"
}

android {
	namespace = "com.tzf.safekeyboard"
	compileSdk = 36

	defaultConfig {
		minSdk = 23
		consumerProguardFiles("consumer-rules.pro")
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
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		viewBinding = true
	}
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
}


