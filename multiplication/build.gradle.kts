plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "ru.profitsw2000.multiplication"
}

dependencies {
    //Modules
    implementation(project(Modules.core))
    implementation(project(Modules.data))
    implementation(project(Modules.navigator))

    //Kotlin
    implementation(Kotlin.core)
    //Design
    implementation(Design.appCompat)
    implementation(Design.material)
    implementation(Design.constraintLayout)
    //ViewModel
    implementation(ViewModel.liveData)
    implementation(ViewModel.viewModel)
    //Legacy
    implementation(Legacy.legacy)
    //GSON
    implementation(GSON.gson)
    //RxJava
    implementation(RxJava.rxJava)
    implementation(RxJava.rxAndroid)
    implementation(RxJava.rxKotlin)
    //Koin
    implementation(Koin.koin)
    //Room
    implementation(Room.roomRuntime)
    implementation(Room.roomKtx)
    implementation(Room.roomRxJava)
    kapt(Room.roomCompiler)
    //Test
    testImplementation(TestImpl.jUnit)
    androidTestImplementation(TestImpl.extJUnit)
    androidTestImplementation(TestImpl.espresso)
}