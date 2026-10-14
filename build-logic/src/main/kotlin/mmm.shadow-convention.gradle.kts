import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("java-library")
    id("com.gradleup.shadow")
}

val shadowCommon = configurations.create("shadowCommon").apply {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val obfuscated = sc(project).current.parsed < "26"

tasks {
    shadowJar {
        archiveClassifier.set(if (obfuscated && isFabric) "dev-shadow" else null)

        configurations.set(listOf(shadowCommon))

        exclude("META-INF/maven/**/*", "META-INF/versions/**/*")

        mergeServiceFiles()
    }

    if (obfuscated && isFabric) named<RemapJarTask>("remapJar") {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        dependsOn(shadowJar)
    }
}

configurations {
    val out =
        if (obfuscated && isFabric) tasks.named<RemapJarTask>("remapJar") else tasks.shadowJar
    apiElements {
        outgoing.artifacts.clear()
        outgoing.artifact(out)
    }
    runtimeElements {
        outgoing.artifacts.clear()
        outgoing.artifact(out)
    }
}