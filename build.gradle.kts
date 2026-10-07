plugins {
    id("java-library")
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.shadow)
}

group = "io.github.aleksireede"
version  = "1.7.8"

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.vault.api)
    compileOnly(libs.playerpoints.api)
    
    implementation(libs.triumph.gui)
    compileOnly(libs.adventure.api)
    
    implementation(libs.helper)
    implementation(libs.acf)
    implementation(libs.bstats)
    library(libs.boosted.yml)
    library(libs.nbt.api)
    
    library(libs.annotations)
}

bukkit {
    name = "EnchantGUI"
    version = project.version.toString()
    main = "io.github.aleksireede.enchantgui.EnchantGUIPlugin"
    apiVersion = "26.2"
    website = "https://github.com/aleksireede/EnchantGUI"
    authors = listOf("aleksireede")
    softDepend = listOf("Vault", "PlayerPoints")
    
    permissions {
        register("eshop.use") {
            description = "Gives access to /eshop."
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
        register("eshop.reload") {
            description = "Gives access to /eshop reload."
        }
        register("eshop.all") {
            description = "Gives access to all enchants and all levels of the enchants."
        }
        register("eshop.enchanting-table") {
            description = "Gives access to the right click on enchanting table feature."
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks {
    jar {
        enabled = false
    }

    build {
        dependsOn(shadowJar)
    }
    
    shadowJar {
        // ACF (and similar libs) use reflection; minimize strips required classes.
        archiveFileName.set("enchantgui.jar")
        
        relocate("org.bstats", "io.github.aleksireede.enchantgui.util")
        relocate("co.aikar.commands", "io.github.aleksireede.enchantgui.acf")
        relocate("co.aikar.locales", "io.github.aleksireede.enchantgui.locales")
        relocate("com.github.sarhatabaot.kraken", "io.github.aleksireede.enchantgui.kraken")
        relocate("dev.triumphteam", "io.github.aleksireede.enchantgui.gui")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}
