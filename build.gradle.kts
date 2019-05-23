import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.1.5.RELEASE" apply false
    id("io.spring.dependency-management") version "0.6.0.RELEASE" apply false
    kotlin("jvm") version "1.2.71"
    kotlin("plugin.spring") version "1.2.71" apply false
    // apply false: 플러그인을 현재 프로젝트에 적용하지 않고 subprojects 블록에서 사용
}

allprojects {
    repositories {
        jcenter()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "style.everywear"
    version = "1.0.0"
    java.sourceCompatibility = JavaVersion.VERSION_1_8

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
}

project(":ew-synthesis") {
    dependencies {
        implementation(project(":ew-core"))
    }
}

project(":ew-fitting") {
    dependencies {
        implementation(project(":ew-core"))
    }
}