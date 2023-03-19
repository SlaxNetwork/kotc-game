plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"

    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"}

group = "io.github.slaxnetwork"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    compileOnly("io.github.slaxnetwork:bukkit-core-api:0.0.1")
    compileOnly("io.github.slaxnetwork:kyouko-wrapper:0.0.1")

    implementation("me.tech:chestuiplus:1.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.11.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.11.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
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
    }
}