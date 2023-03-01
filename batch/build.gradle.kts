val commonsCollections4Version: String by project
val commonsLang3Version: String by project
val cpeParserVersion: String by project
val packageUrlJavaVersion: String by project

dependencies {
    implementation(project(":model"))
    implementation(project(":interface"))
    implementation(project(":dao"))
    implementation(project(":utility"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.yaml:snakeyaml")
    implementation("org.apache.commons:commons-collections4:$commonsCollections4Version")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    implementation("us.springett:cpe-parser:$cpeParserVersion")
    implementation("com.github.package-url:packageurl-java:$packageUrlJavaVersion")

    runtimeOnly("org.springframework.boot:spring-boot-starter-aop")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}