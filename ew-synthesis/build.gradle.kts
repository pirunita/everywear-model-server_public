plugins {
    id("com.google.cloud.tools.jib") version "1.2.0"
}

extra["springCloudVersion"] = "Greenwich.SR1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // TensorFlow CPU
    implementation("org.tensorflow:tensorflow:1.13.1")

    // TensorFlow GPU
//    implementation("org.tensorflow:libtensorflow:1.13.1")
//    implementation("org.tensorflow:libtensorflow_jni_gpu:1.13.1")

    // Spring Cloud Stream
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")

    // Spring Boot DevTools
    implementation("org.springframework.boot:spring-boot-devtools")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

jib {
    from {
        image = "gcr.io/distroless/java:debug"
    }
    to {
        image = "jun097kim/${project.name}"
        tags = setOf("$version", "latest")
    }
    container.volumes = listOf("/app/uploads")
    extraDirectories.setPaths("jib-extras")
}
