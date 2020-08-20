plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.4.0"
}


tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation(project(":gltf-api"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jsoup:jsoup:1.12.2")

    implementation("com.adrienben.tools:gltf-loader:1.0.6-alpha3")
    implementation("org.jmonkeyengine:jme3-core:3.2.2-stable")
    implementation("com.github.dwursteisen.kotlin-math:kotlin-math-jvm:1.0.0-alpha18")
    implementation("org.l33tlabs.twl:pngdecoder:1.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}
