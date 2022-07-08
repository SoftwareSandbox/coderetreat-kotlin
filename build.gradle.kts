plugins {
    id("kotlin-conventions")
}

dependencies {
    implementation("dev.forkhandles:result4k:2.2.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("io.mockk:mockk:1.12.0")
}
repositories {
    mavenCentral()
}
