plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    id("publish")
}

group = "com.actito.gradle"
version = libs.versions.actito.services.get()

dependencies {
    compileOnly(libs.android.gradle.api)
    implementation(libs.google.gson)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly(libs.android.gradle.api)
}

gradlePlugin {
    website = "https://actito.com/"
    vcsUrl = "https://https://github.com/actito/actito-services-gradle-plugin/"

    plugins {
        create("actitoServicesGradlePlugin") {
            id = "com.actito.gradle.actito-services"
            implementationClass = "com.actito.gradle.ActitoServicesGradlePlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "Local"
            url = uri(layout.buildDirectory.dir("repo").map { it.asFile.path })
        }
    }
}

tasks.withType<Test>().configureEach {
    dependsOn("publishAllPublicationsToLocalRepository")

    systemProperty("pluginRepo", layout.buildDirectory.dir("repo").get().asFile.absolutePath)
    systemProperty("pluginVersion", version)
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}
