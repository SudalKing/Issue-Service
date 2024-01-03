import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2-SNAPSHOT" apply false
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    kotlin("plugin.jpa") version "1.9.21"

    kotlin("kapt") version "1.6.21"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

allprojects {
    group = "com.fastcampus"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

// 하위 모듈에서도 사용할 수 있도록
subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-kapt")

    dependencies {
        // JWT 인증
        implementation("com.auth0:java-jwt:3.19.2")

        // kotlin logger (@Slf4j 대신)
        implementation("io.github.microutils:kotlin-logging:1.12.5")

        // kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // H2DB
        runtimeOnly("com.h2database:h2")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
}



tasks.withType<Test> {
    useJUnitPlatform()
}
