val packageUrlJavaVersion: String by project

dependencies {
    implementation(project(":interface"))
    implementation(project(":model"))
    implementation(project(":utility"))
    implementation(project(":cache"))
    implementation(project(":dao"))

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}