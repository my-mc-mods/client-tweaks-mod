@file:Suppress("unused")

import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun sc(project: Project) = project.extensions.findByType(StonecutterBuildExtension::class.java)!!

val Project.isFabric
    get() = sc(project).current.project.endsWith("-fabric")
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
val Project.isLinux: Boolean
    get() = System.getProperty("os.name").lowercase().contains("linux")

fun RepositoryHandler.strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
    forRepository { maven(url) { name = alias } }
    filter { groups.forEach(::includeGroup) }
}