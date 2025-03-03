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

publishing {
    publications {
        create<MavenPublication>("release") {
            artifact("$buildDir/outputs/aar/fflibrary-release.aar")
            //from(components["release"])
            //from(components.findByName("release"))//findByName("release") ?: return@create
            groupId = "com.github.theitlion5891"
            artifactId = "fflibrary"
            version = "1.0.1"

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
    //implementation ("com.github.florent37:viewtooltip:1.2.2")

   // implementation ("com.commit451:PhotoView:1.2.4")

    implementation ("com.karumi:dexter:6.2.3")

    implementation ("commons-io:commons-io:2.7")

    // implementation 'com.github.dhaval2404:imagepicker-support:1.7.1'

    //Cash free SDK
    implementation ("com.cashfree.pg:api:2.0.4")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")
    //implementation 'com.cashfree.pg:android-sdk:1.5.1'

    //doGPayPayment
    // implementation files ("libs/opencsv-5.4.jar")
    //implementation files ("libs/google-pay-client-api-1.0.0.aar")
    implementation ("com.google.android.gms:play-services-tasks:18.0.2")
    //doAmazonPayment
    implementation ("androidx.browser:browser:1.2.0")

    //implementation 'com.razorpay:checkout:1.5.16'

    implementation ("com.paytm.appinvokesdk:appinvokesdk:1.5.4")

    implementation ("androidx.work:work-runtime:2.7.1")

    //firebase
    /*implementation ("com.google.firebase:firebase-messaging:23.1.2'
    implementation platform('com.google.firebase:firebase-bom:31.4.0')
    implementation 'com.google.firebase:firebase-crashlytics'
    implementation 'com.google.firebase:firebase-analytics'*/


    //tooltip
   // implementation ("com.github.florent37:viewtooltip:1.2.2")

    implementation ("com.github.nkzawa:socket.io-client:0.6.0")

    // implementation project(':unityLibrary')

    implementation ("com.github.tapadoo:alerter:7.2.4")

    /*implementation 'com.github.hyuwah:DraggableView:1.0.0'

    implementation "com.hanks:htextview-typer:0.1.6"*/

    //Sneak
    //sneaker
   // implementation ("com.irozon.sneaker:sneaker:2.0.0")

    // Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    //OTP View
    //implementation ("com.github.mukeshsolanki:android-otpview-pinview:2.1.0")

    // add these lines in your app build.gradle
    implementation ("com.google.android.gms:play-services-auth:20.4.1")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.1")

    implementation ("com.itextpdf:itextg:5.5.10")

    implementation ("com.github.yalantis:ucrop:2.2.7")

    implementation ("org.greenrobot:eventbus-java:3.3.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.google.guava:guava:27.0.1-android")

   // implementation ("com.danikula:videocache:2.7.1")

    implementation ("phonepe.intentsdk.android.release:IntentSDK:2.4.2")

    implementation ("com.google.android.gms:play-services-location:18.0.0")

    implementation ("com.google.firebase:firebase-core:21.1.1")
    implementation ("com.google.firebase:firebase-ml-vision:15.0.0")
    implementation ("com.google.firebase:firebase-analytics")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")


}