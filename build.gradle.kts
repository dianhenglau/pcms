/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * User Manual available at https://docs.gradle.org/5.6.2/userguide/tutorial_java_projects.html
 */

plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    application

    // Apply the checkstyle plugin to add support for coding style check
    checkstyle

    // Apply the pmd plugin to add support for code static analyzing
    pmd

    // Apply the jacoco plugin to add code coverage calculation
    jacoco

    // Apply the spotbugs plugin to add support for bug discovery
    id("com.github.spotbugs") version "2.0.1"
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:28.0-jre")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    // To use spotbugs annotation.
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.0.0-beta4")

    // To use mockito
    testImplementation("org.mockito:mockito-junit-jupiter:3.2.0")

    // itext pdf
    implementation("com.itextpdf:itext7-core:7.1.8")
}

application {
    // Define the main class for the application
    mainClassName = "pcms.App"
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}

checkstyle {
    toolVersion = "8.24"
    maxWarnings = 0
}

tasks.withType(Checkstyle::class) {
    reports {
        xml.setEnabled(false)
        html.setEnabled(false)
    }
}

pmd {
    toolVersion = "6.20.0"
    setConsoleOutput(true)
    setRuleSetFiles(files("config/pmd/ruleset.xml"))
    setRuleSets(listOf())
}

tasks.withType(Pmd::class) {
    reports {
        xml.setEnabled(false)
        html.setEnabled(false)
    }
}

spotbugs {
    toolVersion = "4.0.0-beta4"
    excludeFilter = file("config/spotbugs/exclude.xml")
}

tasks.withType(com.github.spotbugs.SpotBugsTask::class) {
    reports {
        xml.setEnabled(false)
        emacs.setEnabled(true)
    }
    // Print report
    finalizedBy(task("${name}Report") {
        mustRunAfter(this@withType)
        doLast {
            this@withType
                .reports
                .emacs
                .destination
                .takeIf { it.exists() }
                ?.readText()
                .takeIf { !it.isNullOrBlank() }
                ?.also { logger.warn(it) }
        }
    })
}

tasks.withType(JavaCompile::class) {
    options.setDeprecation(true)
    options.getCompilerArgs().add("-Xlint:unchecked")
}
