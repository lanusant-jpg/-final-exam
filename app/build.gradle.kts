plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.w03"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.w03"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- ⬇️ 여기에 라이브러리 6개가 추가되었습니다 ⬇️ ---

    // 1. Retrofit (API 통신)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 2. Gson (JSON <-> 코틀린 객체 자동 변환)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 3. Coroutines (비동기 처리)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // 4. ViewModel (데이터 관리)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // 5. LiveData (데이터 변경 감지 -> Compose에서 관찰)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // 6. Coil (Compose에서 인터넷 이미지 로드용)
    implementation("io.coil-kt:coil-compose:2.5.0")
}