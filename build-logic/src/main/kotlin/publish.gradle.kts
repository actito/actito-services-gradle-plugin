plugins {
    id("com.vanniktech.maven.publish")
}

afterEvaluate {
    mavenPublishing {
        publishToMavenCentral()
        signAllPublications()

        coordinates(
            groupId = project.group.toString(),
            artifactId = project.name,
            version = project.version.toString(),
        )

        pom {
            name.set("Actito Services Gradle Plugin")
            description.set("Gradle plug-in to configure Android applications with an actito-services.json file.")
            url.set("https://github.com/actito/actito-services-gradle-plugin/")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/license/mit")
                    distribution.set("https://opensource.org/license/mit")
                }
            }

            developers {
                developer {
                    id.set("Actito")
                    name.set("Actito")
                    url.set("https://github.com/actito/")
                }
            }

            scm {
                url.set("https://github.com/actito/actito-services-gradle-plugin/")
                connection.set("scm:git:git://github.com/actito/actito-services-gradle-plugin.git")
                developerConnection.set("scm:git:ssh://git@github.com/actito/actito-services-gradle-plugin.git")
            }
        }
    }
}
