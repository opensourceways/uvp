plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("java")
    id("war")
}

group = "org.opensourceway.uvp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val packageUrlJavaVersion: String by project
val annotationVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":interface"))
    implementation(project(":dao"))
    implementation(project(":quartz"))
    implementation(project(":utility"))
    implementation(project(":clients"))
    implementation(project(":cache"))
    implementation(project(":batch"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.retry:spring-retry")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")

    runtimeOnly("org.springframework.boot:spring-boot-starter-aop")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
}

springBoot {
    mainClass.set("org.opensourceway.uvp.UvpApplication")
}

allprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.logging.log4j:log4j-api")
        implementation("org.apache.logging.log4j:log4j-core")
        implementation("org.apache.logging.log4j:log4j-slf4j2-impl")
        implementation("org.slf4j:slf4j-api")
        implementation("org.jetbrains:annotations:$annotationVersion")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    configurations {
        all {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}