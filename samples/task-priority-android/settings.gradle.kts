import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        val ldmSdkRepository = providers.gradleProperty("ldmSdkRepository")
            .orElse(providers.environmentVariable("LDM_SDK_REPOSITORY"))
            .orNull
            ?: error(
                "LDM SDK publication is pending redistribution clearance. " +
                    "Provide an explicitly approved staging Maven repository with " +
                    "-PldmSdkRepository=<uri> or LDM_SDK_REPOSITORY."
            )

        maven {
            url = uri(ldmSdkRepository)
        }
    }
}

rootProject.name = "LDMTaskPrioritySample"
include(":app")
