dependencies {
    implementation(project(":model"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}