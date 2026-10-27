plugins {
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version libs.versions.neoforged.moddev.get() apply false
    id("com.gradleup.shadow") version libs.versions.shadow.get() apply false
}

stonecutter active file("versions/current")

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()

        fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
            forRepository { maven(url) { name = alias } }
            filter { groups.forEach(::includeGroup) }
        }
        strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
        strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
        strictMaven("https://maven.parchmentmc.org", "ParchmentMC", "org.parchmentmc.data")
    }

    tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>().configureEach {
        // semver4j
        relocate("org.semver4j", "${"mod.group"()}.shadowed.semver4j")
    }
}

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)

    properties.tags(version, loader)
    constants.match(loader, "fabric", "neoforge", "forge")

    swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = properties.get<String>("mod.id") != "template"
    constants["modmenu"] = properties.getOrNull<String>("deps.modmenu") != null
    constants["cloth_config"] = properties.getOrNull<String>("deps.cloth_config") != null
    dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"

    replacements {
        string(current.parsed >= "1.21.9") {
            replace("DetectedVersion.tryDetectVersion().getName()", "DetectedVersion.tryDetectVersion().name()")
            replace("FMLEnvironment.dist", "FMLEnvironment.getDist()")
            replace("LoadingModList.get()", "FMLLoader.getCurrent().getLoadingModList()")
        }

        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            replace("import net.minecraft.Util;", "import net.minecraft.util.Util;")
            replace("net.minecraft.Util.", "net.minecraft.util.Util.")
            replace("FMLLoader.isProduction()", "FMLLoader.getCurrent().isProduction()")
            replace("FMLLoader.getDist()", "FMLLoader.getCurrent().getDist()")
            replace("org.jetbrains.annotations.Nullable", "org.jspecify.annotations.Nullable")
            replace("org.jetbrains.annotations.NotNull", "org.jspecify.annotations.NonNull")
            replace("@NotNull", "@NonNull")
        }

        string(current.parsed >= "26.1") {
            replace("classTweaker v2 named", "classTweaker v2 official")
            replace("net.minecraft.client.gui.GuiGraphics", "net.minecraft.client.gui.GuiGraphicsExtractor")
            replace("graphics.drawString(", "graphics.text(")
        }

        string(current.parsed >= "26.2") {
            replace("minecraft.setScreen(", "minecraft.gui.setScreen(")
            replace("Minecraft.getInstance().setScreen(", "Minecraft.getInstance().gui.setScreen(")
            replace("minecraft.getToastManager()", "minecraft.gui.toastManager()")
        }
        string(current.parsed > "26.3") {
            replace(
                "net.minecraft.client.renderer.RenderPipelines",
                "net.minecraft.client.renderer.pipeline.RenderPipelines"
            )
        }
    }
}

operator fun String.invoke(): String = project.ext[this] as? String ?: error("No property \"$this\"")