
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    kapt("org.springframework.boot:spring-boot-configuration-processor") // 설정을 config 로 관리하기 위함

    implementation("at.favre.lib:bcrypt:0.9.0")

    runtimeOnly("io.r2dbc:r2dbc-h2")
}