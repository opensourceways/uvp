val guavaVersion: String by project
val springContextSupportVersion: String by project

dependencies {
    implementation(project(":model"))

    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("com.google.guava:guava:$guavaVersion")
    implementation("org.springframework:spring-context-support:$springContextSupportVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}