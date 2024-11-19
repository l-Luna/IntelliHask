plugins {
	id("java")
	id("antlr")
	id("org.jetbrains.kotlin.jvm") version "1.9.25"
	id("org.jetbrains.intellij.platform") version "2.1.0"
}

group = "how.polyfauna"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
	
	intellijPlatform {
		defaultRepositories()
	}
}

dependencies {
	antlr("org.antlr:antlr4:4.11.1")
	
	implementation("org.antlr:antlr4-intellij-adaptor:0.1")
}

dependencies {
	intellijPlatform {
		instrumentationTools()
		intellijIdeaCommunity("2024.3")
	}
}

tasks {
	// Set the JVM compatibility versions
	withType<JavaCompile> {
		sourceCompatibility = "21"
		targetCompatibility = "21"
	}
	withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions.jvmTarget = "21"
	}
	
	patchPluginXml {
		sinceBuild.set("243")
		untilBuild.set("243.*")
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
	outputDirectory = File("./build/generated-src/antlr/main/polyfauna/intellihask/antlr_generated")
	arguments = arguments + listOf("-visitor", "-package", "polyfauna.intellihask.antlr_generated", "-lib", "./build/generated-src/antlr/main/polyfauna/intellihask/antlr_generated/")
}