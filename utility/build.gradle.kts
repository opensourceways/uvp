val packageUrlJavaVersion: String by project
val cvssCalculatorVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":dao"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")
    implementation("us.springett:cvss-calculator:$cvssCalculatorVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}