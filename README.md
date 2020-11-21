# POC: Gradle Artifactory

It demonstrates how to deploy a JAR file to Artifactory using Gradle Kotlin DSL.

We want to compile a Java application, generate a JAR file using the Shadow Jar Gradle plugin and upload it to any Artifactory instance.

All configuration should be set via environment variables or via properties file (`gradle.properties`). We may set them using the Gradle command line arguments too.

## How to run

| Description | Command |
| :--- | :--- |
| Provision Artifactory | `make provision` |
| Destroy Artifactory | `make destroy` |
| Build and deploy the JAR | `make publish` |
| Download and run the JAR | `make test` |
