package com.jsloane.littleone.buildsrc

object Versions {
    const val ktlint = "0.42.1"
}

object Libs {
    object Tools {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.3"
        const val desugaring = "com.android.tools:desugar_jdk_libs:1.1.5"
    }

    object Accompanist {
        const val version = "0.20.1"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val flowlayouts = "com.google.accompanist:accompanist-flowlayout:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val indicators = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Kotlin {
        private const val version = "1.5.31"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.5.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Material {
        const val material = "com.google.android.material:material:1.4.0"
    }

    object PlayServices {
        const val auth = "com.google.android.gms:play-services-auth:19.2.0"
    }

    object Firebase {
        const val gradlePlugin = "com.google.gms:google-services:4.3.10"
        const val bom = "com.google.firebase:firebase-bom:29.0.0"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
    }

    object Hilt {
        private const val version = "2.40"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val testing = "com.google.dagger:hilt-android-testing:$version"
        const val compose = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object Compose {
            const val version = "1.1.0-beta03"

            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val material = "androidx.compose.material:material:$version"
            const val animation = "androidx.compose.animation:animation:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
        }

        object UI {
            const val activityCompose = "androidx.activity:activity-compose:1.4.0"
            const val appcompat = "androidx.appcompat:appcompat:1.3.1"
            const val constraintLayoutCompose =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02"
        }

        object Data {
            const val datastore = "androidx.datastore:datastore-preferences:1.0.0"
        }

        object Lifecycle {
            private const val version = "2.4.0"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Navigation {
            const val navigationCompose = "androidx.navigation:navigation-compose:2.4.0-alpha10"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            object Ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        }
    }

    object JUnit {
        private const val version = "4.13.2"
        const val junit = "junit:junit:$version"
    }

    object Coil {
        const val coilCompose = "io.coil-kt:coil-compose:1.4.0"
    }
}