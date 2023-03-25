import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"

    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val githubActor = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
val githubToken = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")

group = "io.github.slaxnetwork"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")

    // github packages
    setOf(
        "SlaxNetwork/kyouko-kt-wrapper",
        "SlaxNetwork/bukkit-core",
        "SlaxNetwork/mc-chestui-plus"
    ).forEach {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$it")
            credentials {
                username = githubActor
                password = githubToken
            }
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    compileOnly("io.github.slaxnetwork:bukkit-core-api:0.0.2")
    implementation("io.github.slaxnetwork:bukkit-utilities:0.0.3")
    // loaded via bukkit-core
    compileOnly("io.github.slaxnetwork:kyouko-wrapper:0.0.3")

    implementation("me.tech:mc-chestui-plus:0.0.4")

    compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.11.0")
    compileOnly("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.11.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    shadowJar {
        exclude("kotlin/**/*.class")
    }
}

bukkit {
    name = "kotc-game"
    apiVersion = "1.19"
    authors = listOf("Tech")
    main = "io.github.slaxnetwork.KOTCGame"
    depend = listOf("bukkit-core")
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    commands {
        register("test")
        register("endgame")
        register("showtesttitle")
        register("concludevote")
        register("startvote")
        register("vote")
        register("sbtest")
        register("swapsbtoggle")
    }
}