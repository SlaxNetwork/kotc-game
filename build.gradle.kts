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

    maven("https://repo.purpurmc.org/snapshots")

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/SlaxNetwork/kyouko-kt-wrapper")
        credentials {
            username = githubActor
            password = githubToken
        }
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/bukkit-core")
        credentials {
            username = githubActor
            password = githubToken
        }
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/mc-chestui-plus")
        credentials {
            username = githubActor
            password = githubToken
        }
    }
}

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.19.3-R0.1-SNAPSHOT")

    compileOnly("io.github.slaxnetwork:bukkit-core:0.0.1")
    compileOnly("io.github.slaxnetwork:kyouko-wrapper:0.0.1")

    implementation("me.tech:mc-chestui-plus:0.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.10.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.10.0")
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
    }
}