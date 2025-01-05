---
outline: deep
---

# Adding Immersive Messages to Your Project

You can download releases from [Modrinth](https://modrinth.com/mod/immersive-messages-api), but it is also available from my own Maven, 
which is easier to set up for multiversion projects. You will also need [TxniLib](https://www.curseforge.com/minecraft/mc-mods/txnilib).

Snippets are given in Gradle Kotlin DSL for Loom.

::: code-group
```kts [build.gradle.kts]
// in your dependencies block
modImplementation("toni.immersivemessages:${loader}-${mcVersion}:${immersiveMessagesVersion}") { isTransitive = false }
modImplementation("toni.txnilib:${loader}-${mcVersion}:${txnilibVersion}")

// Example versions (check latest on Modrinth)
// immersiveMessagesVersion=1.0.12
// txnilibVersion=1.0.20

// in your repositories block
repositories {
    maven("https://maven.txni.dev/")
    maven("https://maven.su5ed.dev/releases")
}
``` 
:::

# Caxton Custom Fonts

If you want to use Caxton for custom fonts, some additional setup is unfortunately required to properly load all of its dependencies.

::: code-group
```kts [build.gradle.kts]
// in your dependencies block
modImplementation("maven.modrinth:caxton:0.6.0-alpha.2.1+1.20.1-FABRIC")
modImplementation("com.github.ben-manes.caffeine:caffeine:3.1.2")

// if you are on 1.20.1, you will need MixinExtras
include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.4.1")!!)!!)

// if you are on Fabric, you will need Fabric ASM
modImplementation(include("com.github.Chocohead:Fabric-ASM:v2.3") {
    exclude(group = "net.fabricmc", module = "fabric-loader")
})
``` 
:::

Due to differences in implementing text rendering for both Caxton and Vanilla, there may be inconsistencies or things that only
work with one renderer. If you encounter anything that is not the same, please file a bug report.

---
---

For more on API usage, check out the [next page](/api).
