import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.GroovyObject
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.DoubleDelegateWrapper
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("maven-publish")
    id("com.jfrog.artifactory") version "4.18.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j", "log4j-api", properties["version.log4j"].toString())
    implementation("org.apache.logging.log4j", "log4j-core", properties["version.log4j"].toString())
    implementation("org.slf4j", "slf4j-simple", properties["version.slf4j"].toString())
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<ApplicationPluginConvention> {
    mainClassName = "com.example.Main"
}

tasks.withType<ShadowJar> {
    archiveBaseName.set(project.name)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("applicationJar") {
            groupId = "com"
            artifactId = "example"
            version = "0.1.0"

            from(components.findByName("shadow"))
        }
    }
}

configure<ArtifactoryPluginConvention> {
    setContextUrl(System.getenv("ARTIFACTORY_URL") ?: properties["artifactory.url"])
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<DoubleDelegateWrapper> {
            setProperty("repoKey", System.getenv("ARTIFACTORY_REPOSITORY") ?: properties["artifactory.repository"])
            setProperty("username", System.getenv("ARTIFACTORY_USERNAME") ?: properties["artifactory.username"])
            setProperty("password", System.getenv("ARTIFACTORY_PASSWORD") ?: properties["artifactory.password"])
            setProperty("maven", true)
        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", "applicationJar")
            setProperty("publishPom", true)
            setProperty("publishArtifacts", true)
        })
    })
}
