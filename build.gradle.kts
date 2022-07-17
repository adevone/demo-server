plugins {
    alias(libs.plugins.kotlinJvm)
    application
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kotlinxSerialization)
}

group = "io.adev"
version = "0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.adev.demo.server.AppKt")
}

dependencies {
    implementation(libs.bundles.ktor)
    implementation(libs.sqlDelightJdbcDriver)
    implementation(libs.postgresDriver)
    implementation(libs.kotlinxSerializationJson)
}

sqldelight {
    database("Database") {
        // This will be the name of the generated database class.
        packageName = "io.adev.demo.server"
        dialect = "app.cash.sqldelight:postgresql-dialect:${libs.versions.sqlDelight.get()}"
    }
}