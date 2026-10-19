plugins {
 id("com.android.application")
 id("org.jetbrains.kotlin.plugin.compose")
 id("org.jetbrains.kotlin.plugin.serialization")
}
android {
 namespace = "co.za.discovery"
 compileSdk = 37
 defaultConfig { applicationId = "co.za.discovery"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0"; testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
 buildTypes { getByName("debug") { buildConfigField("boolean", "SIMULATE_FAILURE", providers.gradleProperty("simulateFailure").orElse("false").get()) }; getByName("release") { buildConfigField("boolean", "SIMULATE_FAILURE", "false") } }
 buildFeatures { compose = true; buildConfig = true }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
dependencies {
 implementation(platform(libs.androidx.compose.bom))
 implementation(libs.androidx.activity.compose)
 implementation(libs.androidx.compose.material3)
 implementation(libs.androidx.compose.material)
 implementation(libs.androidx.compose.material.icons.extended)
 implementation(libs.androidx.compose.ui)
 implementation(libs.androidx.compose.ui.tooling.preview)
 debugImplementation(libs.androidx.compose.ui.tooling)
 implementation(libs.androidx.lifecycle.runtime.compose)
 implementation(libs.androidx.lifecycle.viewmodel.compose)
 implementation(libs.kotlinx.serialization.json)
 implementation(libs.kotlinx.coroutines.android)
 implementation(libs.coil.compose)
 testImplementation(libs.junit)
 testImplementation(libs.kotlinx.coroutines.test)
}
