val commonsLang3Version: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":batch"))
    implementation(project(":utility"))

    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}