plugins {
    id("com.google.cloud.tools.jib") version "1.2.0"
}

extra["springCloudVersion"] = "Greenwich.SR1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring Cloud Stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")

    // Spring Boot DevTools
    implementation("org.springframework.boot:spring-boot-devtools")

    // Firebase Admin SDK
    implementation("com.google.firebase:firebase-admin:6.8.0")

    // Inject configuration properties
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

jib {
    from {
        image = "gcr.io/distroless/java:debug"
        // 디버깅용 이미지
        // docker exec -it CONTAINER_ID /busybox/sh
    }
    to {
        image = "jun097kim/${project.name}"
        tags = setOf("$version", "latest")
    }
    container.volumes = listOf("/app/uploads")  // 마운트 포인트
    extraDirectories.setPaths("jib-extras")     // jib-extras 디렉토리 내의 모든 파일이 이미지의 루트 디렉토리로 복사됨
}
