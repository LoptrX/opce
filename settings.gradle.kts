rootProject.name = "opmce"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://repo.huaweicloud.com/repository/maven/")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
//        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://repo.huaweicloud.com/repository/maven/")
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

include(":composeApp")