plugins { `kotlin-dsl` }

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.spring.boot.gradle.plugin)
    implementation(libs.dependency.management.plugin)
}
