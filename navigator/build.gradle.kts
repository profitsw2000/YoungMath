plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "youngmath.navigator"
}

dependencies {
    //Kotlin
    implementation(Kotlin.core)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}