val packageUrlJavaVersion: String by project
val cvssCalculatorVersion: String by project
val jasyptVersion: String by project
val commonsLang3Version: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":dao"))
    implementation(project(":cache"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")
    implementation("us.springett:cvss-calculator:$cvssCalculatorVersion")
    implementation("org.jasypt:jasypt:$jasyptVersion")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}