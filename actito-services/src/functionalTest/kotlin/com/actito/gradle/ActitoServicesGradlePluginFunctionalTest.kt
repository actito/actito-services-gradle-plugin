package com.actito.gradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.util.GradleVersion
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// AGP & Gradle versions
// https://developer.android.com/build/releases/gradle-plugin#updating-gradle

private const val LATEST_AGP_VERSION = "8.12.0"
private const val LATEST_GRADLE_VERSION = "8.13"

class ActitoServicesGradlePluginFunctionalTest {

    @field:TempDir
    lateinit var tempDir: File

    private val pluginRepo = System.getProperty("pluginRepo")
    private val pluginVersion = System.getProperty("pluginVersion")

    @Test
    fun `process debug variant file`() {
        val project = loadProject("app-no-flavors", LATEST_AGP_VERSION)

        val runner = createGradleRunner(GradleVersion.version(LATEST_GRADLE_VERSION), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
        assertTrue(result.output.contains("Parsing json file: ${project.canonicalPath}/app/src/debug/actito-services.json"))

        assertFileContentsAreEqual(
            expected = File("src/functionalTest/testData/expectations/res-no-flavor-debug/values.xml"),
            actual = project.resolve("app/build/generated/res/processDebugActitoServices/values/values.xml")
        )
    }

    @Test
    fun `process release variant root file`() {
        val project = loadProject("app-no-flavors", LATEST_AGP_VERSION)

        val runner = createGradleRunner(GradleVersion.version(LATEST_GRADLE_VERSION), task = "assembleRelease")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processReleaseActitoServices")?.outcome)
        assertTrue(result.output.contains("Parsing json file: ${project.canonicalPath}/app/actito-services.json"))

        assertFileContentsAreEqual(
            expected = File("src/functionalTest/testData/expectations/res-no-flavor-release/values.xml"),
            actual = project.resolve("app/build/generated/res/processReleaseActitoServices/values/values.xml")
        )
    }

    @Test
    fun `process multi-flavor demo debug variant file`() {
        val project = loadProject("app-multi-flavors", LATEST_AGP_VERSION)

        val runner = createGradleRunner(GradleVersion.version(LATEST_GRADLE_VERSION), task = "assembleDemoDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDemoDebugActitoServices")?.outcome)
        assertTrue(result.output.contains("Parsing json file: ${project.canonicalPath}/app/src/demo/actito-services.json"))

        assertFileContentsAreEqual(
            expected = File("src/functionalTest/testData/expectations/res-multi-flavor-demo-debug/values.xml"),
            actual = project.resolve("app/build/generated/res/processDemoDebugActitoServices/values/values.xml")
        )
    }

    @Test
    fun `process file with custom hosts info`() {
        val project = loadProject("app-with-custom-hosts-info", LATEST_AGP_VERSION)

        val runner = createGradleRunner(GradleVersion.version(LATEST_GRADLE_VERSION), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
        assertTrue(result.output.contains("Parsing json file: ${project.canonicalPath}/app/actito-services.json"))

        assertFileContentsAreEqual(
            expected = File("src/functionalTest/testData/expectations/res-with-custom-hosts-info/values.xml"),
            actual = project.resolve("app/build/generated/res/processDebugActitoServices/values/values.xml")
        )
    }

    @Test
    fun `fails when actito services file is missing`() {
        loadProject("app-missing-actito-services", LATEST_AGP_VERSION)

        val runner = createGradleRunner(GradleVersion.version(LATEST_GRADLE_VERSION), task = "assembleDebug")
        val result = runner.buildAndFail()

        assertEquals(TaskOutcome.FAILED, result.task(":app:processDebugActitoServices")?.outcome)
        assertTrue(result.output.contains("File actito-services.json is missing."))
    }

    @Test
    fun `build with AGP 8_0_0`() {
        loadProject("app-no-flavors", agpVersion = "8.0.0")

        val runner = createGradleRunner(GradleVersion.version("8.0"), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
    }

    @Test
    fun `build with AGP 8_2_0`() {
        loadProject("app-no-flavors", agpVersion = "8.2.0")

        val runner = createGradleRunner(GradleVersion.version("8.2"), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
    }

    @Test
    fun `build with AGP 8_3_0`() {
        loadProject("app-no-flavors", agpVersion = "8.3.0")

        val runner = createGradleRunner(GradleVersion.version("8.4"), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
    }

    @Test
    fun `build with AGP 8_5_0`() {
        loadProject("app-no-flavors", agpVersion = "8.5.0")

        val runner = createGradleRunner(GradleVersion.version("8.7"), task = "assembleDebug")
        val result = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":app:processDebugActitoServices")?.outcome)
    }

    private fun loadProject(project: String, agpVersion: String): File {
        File("src/functionalTest/testData/$project").copyRecursively(tempDir)

        val gradlePropertiesFile = tempDir.resolve("gradle.properties")
        gradlePropertiesFile.writeText(
            """
                pluginRepo=$pluginRepo
                pluginVersion=$pluginVersion
                agpVersion=$agpVersion
            """.trimIndent()
        )

        return tempDir
    }

    private fun createGradleRunner(gradleVersion: GradleVersion, task: String = "assembleDebug"): GradleRunner {
        // GradleRunner.withPluginClasspath() won't work, because it puts the
        // plugin in an isolated classloader so it can't interact with AGP.
        // Instead we're passing the path to the built Maven repo.

        return GradleRunner.create()
            .forwardOutput()
            .withGradleVersion(gradleVersion.version)
            .withArguments(task)
            .withProjectDir(tempDir)
    }

    private fun assertFileContentsAreEqual(expected: File, actual: File, message: String? = null) {
        val expectedContent = expected.readText()
        val actualContent = actual.readText()

        assertEquals(expectedContent, actualContent, message)
    }
}
