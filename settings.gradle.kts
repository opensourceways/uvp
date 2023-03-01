rootProject.name = "uvp"

pluginManagement {
    val orgSpringframeworkBootVersion: String by settings
    val ioSpringDependencyManagementVersion: String by settings
    plugins {
        id("org.springframework.boot") version orgSpringframeworkBootVersion
        id("io.spring.dependency-management") version ioSpringDependencyManagementVersion
    }
}

include("model")
include("interface")
include("dao")
include("quartz")
include("utility")
include("clients")
include("cache")
include("batch")
