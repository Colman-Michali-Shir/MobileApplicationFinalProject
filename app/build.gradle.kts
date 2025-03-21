plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
<<<<<<< HEAD
    id("kotlin-kapt")
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.example.foodie_finder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.foodie_finder"
=======
}

android {
    namespace = "com.example.mobile_application_course"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mobile_application_course"
>>>>>>> main
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
<<<<<<< HEAD


        buildConfigField(
            "String",
            "CLOUDINARY_CLOUD_NAME",
            "\"${project.properties["CLOUDINARY_CLOUD_NAME"] ?: ""}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_API_KEY",
            "\"${project.properties["CLOUDINARY_API_KEY"] ?: ""}\""
        )
        buildConfigField(
            "String",
            "CLOUDINARY_API_SECRET",
            "\"${project.properties["CLOUDINARY_API_SECRET"] ?: ""}\""
        )
=======
>>>>>>> main
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
<<<<<<< HEAD
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
=======
>>>>>>> main
}

dependencies {

<<<<<<< HEAD
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

=======
>>>>>>> main
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui)
<<<<<<< HEAD

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.picasso)
    implementation(libs.cloudinary.android)

=======
>>>>>>> main
}