plugins {
	id("java")
	id("antlr")
	id("org.jetbrains.kotlin.jvm") version "1.9.25"
	id("org.jetbrains.intellij") version "1.17.4"
}

group = "luna"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	antlr("org.antlr:antlr4:4.11.1")
	
	implementation("org.antlr:antlr4-intellij-adaptor:0.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
	version.set("2023.2.6")
	type.set("IC") // Target IDE Platform
	
	plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
	// Set the JVM compatibility versions
	withType<JavaCompile> {
		sourceCompatibility = "17"
		targetCompatibility = "17"
	}
	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions.jvmTarget = "17"
	}
	
	patchPluginXml {
		sinceBuild.set("232")
		untilBuild.set("242.*")
	}
	
	signPlugin {
		certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
		privateKey.set(System.getenv("PRIVATE_KEY"))
		password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
	}
	
	publishPlugin {
		token.set(System.getenv("PUBLISH_TOKEN"))
	}
}

tasks.generateGrammarSource{
	outputDirectory = File("./build/generated-src/antlr/main/luna/intellihask/antlr_generated")
	arguments = arguments + listOf("-visitor", "-package", "luna.intellihask.antlr_generated", "-lib", "./build/generated-src/antlr/main/luna/intellihask/antlr_generated/")
}