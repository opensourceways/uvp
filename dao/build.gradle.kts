dependencies {
    implementation(project(":model"))

    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}