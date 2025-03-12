plugins {
    //id("org.jetbrains.kotlin.android" ) version "1.9.24"
    id("com.android.library")
    id("org.jetbrains.kotlin.android") // Kotlin plugin
    id("maven-publish")
}

android {
    namespace = "com.fantafeat"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "VERSION_NAME", "\"1.0.7\"")
        buildConfigField("String", "APPLICATION_ID", "\"com.fantafeat\"")
        buildConfigField("int", "VERSION_CODE", "1016")
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

    lint {
        baseline = file("lint-baseline.xml")
    }

    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            //version "3.10.2";
        }
    }
    buildFeatures {
        buildConfig = true

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }
}

/*java {
    withSourcesJar()
}*/

publishing {
    publications {
        create<MavenPublication>("release") {
            artifact("$buildDir/outputs/aar/fflibrary-release.aar")

            pom {
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                    configurations.getByName("implementation").allDependencies.forEach { dep ->
                        if (dep is ModuleDependency) {
                            dependenciesNode.appendNode("dependency").apply {
                                appendNode("groupId", dep.group)
                                appendNode("artifactId", dep.name)
                                appendNode("version", dep.version)
                                appendNode("scope", "compile")
                            }
                        }
                    }
                }
            }
            //from(components["java"])
            //from(components.findByName("release"))//findByName("release") ?: return@create
            groupId = "com.github.fantafeat"
            artifactId = "fflibrary"
            version = "1.0.21-SNAPSHOT"

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

tasks.named("publishReleasePublicationToMavenLocal").configure {
    dependsOn(tasks.named("bundleReleaseAar"))
}

/*publishing {
    repositories {
        maven {
            name = "Fantafeat"
            url = uri("https://maven.pkg.github.com/theitlion5891/ff_library_project")

            credentials {
                username = System.getenv("GITHUB_USERNAME") ?: "theitlion5891"
                password = System.getenv("GITHUB_TOKEN") ?: "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"
            }
        }
    }
    publications {
        create<MavenPublication>("release") {
            from(components.findByName("release") ?: return@create)
            //artifact("$buildDir/outputs/aar/ff_library_project.aar") //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
            groupId = "com.github.theitlion5891"
            artifactId = "fflibrary"
            version = "1.0.9" //ver

            //artifact("$buildDir/outputs/aar/mylibrary-release.aar") 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
        }
    }
}*/

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.squareup.okhttp3:okhttp:4.8.0")

    //for scalable size
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")

    //image
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.karumi:dexter:6.2.3")

    implementation("com.google.code.gson:gson:2.8.6")

    implementation ("com.github.tapadoo:alerter:7.2.4")

    implementation ("com.google.guava:guava:27.0.1-android")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")


}