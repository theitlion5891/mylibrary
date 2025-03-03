pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("git@github.com:theitlion5891/mylibrary.git")  }
        maven { setUrl("https://jitpack.io")  }
        maven {
            setUrl( "https://artifactory.paytm.in/libs-release-local")
        }
        maven { setUrl("https://maven.cashfree.com/release")}
        maven { setUrl( "https://jitpack.io") }

        maven {
            setUrl(  "https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("git@github.com:theitlion5891/mylibrary.git")  }
        maven { setUrl("https://jitpack.io")  }
        maven {
            setUrl( "https://artifactory.paytm.in/libs-release-local")
        }
        maven { setUrl("https://maven.cashfree.com/release")}
        maven { setUrl( "https://jitpack.io") }

        maven {
            setUrl(  "https://phonepe.mycloudrepo.io/public/repositories/phonepe-intentsdk-android")
        }
    }
}

rootProject.name = "My Application"
include(":app")
//include(":mylibrary")
include(":fflibrary")
