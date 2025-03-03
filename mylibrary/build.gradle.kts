plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")

}

android {
    namespace = "com.bb11.mylibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}


publishing {
    publications {
        create<MavenPublication>("release") {
            artifact("$buildDir/outputs/aar/mylibrary-release.aar")
            //from(components["release"])
            //from(components.findByName("release"))//findByName("release") ?: return@create
            groupId = "com.bb11"
            artifactId = "mylibrary"
            version = "1.0.7-SNAPSHOT"

            //artifact("$buildDir/outputs/aar/mylibrary-release.aar") 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
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/theitlion5891/mylibrary")
            credentials {
                username = System.getenv("GITHUB_USERNAME") ?: "theitlion5891"
                password = System.getenv("GITHUB_TOKEN") ?: "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"
            }
        }
    }

}

dependencies {

    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}