/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.11/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    jacoco
    //id("org.openjfx.javafxplugin") version "0.1.0"
}


repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)

    // For command line arguments
    implementation("info.picocli:picocli:4.7.6")

    // To connect to postgres
    runtimeOnly("org.postgresql:postgresql:42.6.0")

    //for JSON
    implementation("org.json:json:20210307")

    /*Add Mockito for mocking in tests - using Mockito to make our tests more focused and reliable by isolating the logic we’re testing. 
    //It lets us replace external dependencies (like the database calls in TenantIngredients) with mock objects that return controlled data. 
    This way, we can test just the recipe ranking logic without worrying about the database or other external factors, making the tests easier to manage and quicker */
    testImplementation("org.mockito:mockito-core:5.4.0")
    
}
/*
javafx{
    version = "20.0.2"
    modules("javafx.controls", "javafx.fxml")
}*/

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
  
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in` 
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }

    sourceSets(sourceSets.main.get())
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.30".toBigDecimal()
            }
        }
    }
}
