rootProject.name = "EnchantGUI"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven ("https://repo.aikar.co/content/groups/aikar/")
        maven ("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven ("https://nexus.hc.to/content/repositories/pub_releases")
        maven ("https://oss.sonatype.org/content/groups/public/")
        maven ("https://repo.codemc.io/repository/maven-public/")
        maven ("https://jitpack.io")
        maven ("https://repo.rosewooddev.io/repository/public/")
        maven ("https://repo.papermc.io/repository/maven-public/")
    }
    versionCatalogs {
        create("libs") {
            plugin("shadow", "com.gradleup.shadow").version("9.3.0")
            plugin("plugin-yml", "net.minecrell.plugin-yml.bukkit").version("0.6.0")

            library("paper-api", "io.papermc.paper:paper-api:26.2.build.100-stable")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.16.0")
            library("adventure-api", "net.kyori:adventure-api:5.2.0")
            library("triumph-gui", "dev.triumphteam:triumph-gui-paper:3.1.13")
            library("vault-api", "com.github.MilkBowl:VaultAPI:1.7.1")
            library("playerpoints-api", "org.black_ixx:playerpoints:3.3.2")
            library("acf", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("bstats", "org.bstats:bstats-bukkit:3.1.0")
            library("annotations", "org.jetbrains:annotations:26.0.1")
            library("helper", "com.github.sarhatabaot:KrakenCore:1.6.3")

            library("boosted-yml", "dev.dejvokep:boosted-yaml:1.3.7")
        }
    }
}
