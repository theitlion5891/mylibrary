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

        maven { setUrl( "https://jitpack.io") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("git@github.com:theitlion5891/mylibrary.git")  }
        maven { setUrl("https://jitpack.io")  }
    }
}

rootProject.name = "Fantafeat Module"
include(":app")
//include(":mylibrary")
include(":fflibrary")
