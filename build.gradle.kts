import toni.blahaj.api.*

val templateSettings = object : BlahajSettings {

	// -------------------- Dependencies ---------------------- //
	override val depsHandler: BlahajDependencyHandler get() = object : BlahajDependencyHandler {
		override fun addGlobal(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation("com.github.ben-manes.caffeine:caffeine:3.1.2")

			deps.compileOnly("org.projectlombok:lombok:1.18.34")
			deps.annotationProcessor("org.projectlombok:lombok:1.18.34")
		}

		override fun addFabric(mod : ModData, deps: DependencyHandler) {
			if (mod.mcVersion == "1.21.1") {
				deps.modImplementation(modrinth("caxton", "0.6.0-alpha.2+1.21.1-FABRIC"))
				deps.modImplementation(deps.include("com.github.Chocohead:Fabric-ASM:v2.3") {
					exclude(group = "net.fabricmc", module = "fabric-loader")
				})
			}
			else {
				deps.modImplementation(modrinth("caxton", "0.6.0-alpha.2.1+1.20.1-FABRIC"))
				deps.include(deps.implementation(deps.annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.4.1")!!)!!)
				deps.modImplementation(deps.include("com.github.Chocohead:Fabric-ASM:v2.3") {
					exclude(group = "net.fabricmc", module = "fabric-loader")
				})
			}
		}

		override fun addForge(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation(modrinth("caxton", "0.6.0-alpha.2.1+1.20.1-FORGE"))
			deps.minecraftRuntimeLibraries("com.github.ben-manes.caffeine:caffeine:3.1.2")

			deps.compileOnly(deps.annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)
			deps.implementation(deps.include("io.github.llamalad7:mixinextras-forge:0.4.1")!!)
		}

		override fun addNeo(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation(modrinth("caxton", "0.6.0-alpha.2+1.21.1-NEOFORGE"))
			deps.minecraftRuntimeLibraries("com.github.ben-manes.caffeine:caffeine:3.1.2")
		}
	}


	// ---------- Curseforge/Modrinth Configuration ----------- //
	// For configuring the dependecies that will show up on your mod page.
	override val publishHandler: BlahajPublishDependencyHandler get() = object : BlahajPublishDependencyHandler {
		override fun addShared(mod : ModData, deps: DependencyContainer) {
			deps.requires("txnilib")
			if (mod.isFabric) {
				deps.requires("fabric-api")
			}
		}

		override fun addCurseForge(mod : ModData, deps: DependencyContainer) {

		}

		override fun addModrinth(mod : ModData, deps: DependencyContainer) {

		}
	}
}

plugins {
	`maven-publish`
	application
	id("toni.blahaj") version "1.0.8"
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("dev.kikugie.j52j") version "1.0"
	id("dev.architectury.loom")
	id("me.modmuss50.mod-publish-plugin")
	id("systems.manifold.manifold-gradle-plugin")
}

blahaj {
	sc = stonecutter
	settings = templateSettings
	init()
}

// Dependencies
repositories {
	maven("https://www.cursemaven.com")
	maven("https://api.modrinth.com/maven")
	maven("https://thedarkcolour.github.io/KotlinForForge/")
	maven("https://maven.kikugie.dev/releases")
	maven("https://maven.txni.dev/releases")
	maven("https://jitpack.io")
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	maven("https://maven.parchmentmc.org")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.fabricmc.net")
	maven("https://maven.shedaniel.me/")
	maven("https://maven.txni.dev/")
}