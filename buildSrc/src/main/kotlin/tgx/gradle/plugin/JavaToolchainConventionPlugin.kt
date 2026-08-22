package tgx.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

open class JavaToolchainConventionPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.withId("java") {
      project.extensions.findByType(JavaPluginExtension::class.java)?.apply {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
      }
    }
  }
}
