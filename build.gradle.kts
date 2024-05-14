plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.sonarqube") version "4.4.1.3373"
    jacoco
}

sonar {
    properties {
        property("sonar.projectKey", "Pemrograman-lanjut-A1_BE-Auth-User-Staff")
        property("sonar.organization", "pemrograman-lanjut-a1")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}



group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation ("jakarta.servlet:jakarta.servlet-api:4.0.4")
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}
//
//tasks.test {
//    useJUnitPlatform()
//    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
//}
//tasks.jacocoTestReport {
//    classDirectories.setFrom(files(classDirectories.files.map {
//        fileTree(it) { exclude("**/*Application**") }
//    }))
//    dependsOn(tasks.test) // tests are required to run before generating the report
//    reports {
//        xml.required.set(false)
//        csv.required.set(false)
//        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
//    }
//}

tasks.register<Test>("unitTest"){
    description = "Runs unit test."
    group = "verification"

    filter{
        excludeTestsMatching("*FunctionalTest")
    }
}

tasks.register<Test>("functionalTest"){
    description = "Runs functional test."
    group = "verification"

    filter{
        excludeTestsMatching("*FunctionalTest")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.test {
    filter {
        excludeTestsMatching("*FunctionalTest")
    }

    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required = true
        xml.required = true
    }
}