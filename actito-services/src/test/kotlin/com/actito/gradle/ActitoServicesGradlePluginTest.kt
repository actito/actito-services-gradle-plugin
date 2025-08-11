package com.actito.gradle

import kotlin.test.Test
import kotlin.test.assertContains

class ActitoServicesGradlePluginTest {

    @Test
    fun `search locations - no flavor`() {
        val locations = ActitoServicesGradlePlugin.getJsonLocations("release", emptyList())
        assertContains(locations, "src/release/actito-services.json")
    }

    @Test
    fun `search locations - single flavor`() {
        val locations = ActitoServicesGradlePlugin.getJsonLocations("release", listOf("foo"))
        val expected = listOf(
            "src/fooRelease/actito-services.json",
            "src/foo/release/actito-services.json",
            "src/foo/actito-services.json",
            "src/release/foo/actito-services.json",
            "src/release/actito-services.json",
            "actito-services.json",
        )

        for (value in expected) {
            assertContains(locations, value)
        }
    }

    @Test
    fun `search locations - multiple flavors`() {
        val locations = ActitoServicesGradlePlugin.getJsonLocations("release", listOf("foo", "bar"))
        val expected = listOf(
            "src/foo/bar/release/actito-services.json",
            "src/fooBar/release/actito-services.json",
            "src/release/fooBar/actito-services.json",
            "src/foo/release/actito-services.json",
            "src/foo/bar/actito-services.json",
            "src/foo/barRelease/actito-services.json",
            "src/release/actito-services.json",
            "src/fooBar/actito-services.json",
            "src/fooBarRelease/actito-services.json",
            "src/foo/actito-services.json",
            "src/fooRelease/actito-services.json",
            "actito-services.json"
        )

        for (value in expected) {
            assertContains(locations, value)
        }
    }
}
