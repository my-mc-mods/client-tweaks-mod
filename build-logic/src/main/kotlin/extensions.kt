@file:Suppress("unused", "SpellCheckingInspection")

import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.named

fun sc(project: Project) = project.extensions.findByType(StonecutterBuildExtension::class.java)!!
fun loomx(project: Project): dev.kikugie.loomx.LoomCompatProjectExtension =
    project.extensions.findByType(dev.kikugie.loomx.LoomCompatProjectExtension::class.java)
        ?: error("loom-back-compat extension not found on ${project.path}; ensure the dev.kikugie.loom-back-compat plugin is applied before the platform convention")

val Project.isFabric get() = sc(project).current.project.endsWith("-fabric")
val Project.requiredJava: JavaVersion
    get() = when {
        sc(this).current.parsed >= "26.1" -> JavaVersion.VERSION_25
        sc(this).current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
        sc(this).current.parsed >= "1.18" -> JavaVersion.VERSION_17
        sc(this).current.parsed >= "1.17" -> JavaVersion.VERSION_16
        else -> JavaVersion.VERSION_1_8
    }
val Project.compatibleVersions: List<String>
    get() = sc(this).properties.rawOrNull("mod", "mc_releases")?.asList().orEmpty().map { it.toString() }
val Project.isLinux: Boolean get() = System.getProperty("os.name").lowercase().contains("linux")
val Project.modJar get() = if (isFabric) loomx(project).modJar else project.tasks.named<Jar>("jar")

fun RepositoryHandler.strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
    forRepository { maven(url) { name = alias } }
    filter { groups.forEach(::includeGroup) }
}