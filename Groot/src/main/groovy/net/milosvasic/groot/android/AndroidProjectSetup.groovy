package net.milosvasic.groot.android

import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

class AndroidProjectSetup extends ProjectSetup {

    AndroidProjectSetup(Project project) {
        super(project)
        generateBuildConfig = false
    }

    @Override
    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage) {
        setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage, false)
    }

    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage, boolean proguard) {
        super.setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage)
        project.android {
            publishNonDefault true
            compileSdkVersion 25
            buildToolsVersion "25.0.2"
            defaultConfig {
                minSdkVersion 19
                targetSdkVersion 25
                testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
            }
            sourceSets.main.java.srcDirs = [
                    'src/main/java',
                    'src/common/java'
            ]
            buildTypes {
                release {
                    minifyEnabled proguard
                    shrinkResources false
                    if (proguard) {
                        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                    }
                }
                debug {
                    minifyEnabled false
                    shrinkResources false
                }
            }
        }
        project.dependencies {
            compile project.fileTree(dir: 'libs', include: '*.jar')
        }
        setupBuildDestination()
        setupModuleRequire()
        setupReleaseCopy()
    }

    void setup(
            int androidSdkVersion,
            String androidBuildToolsVersion,
            int minSdk,
            int targetSdk,
            String instrumentationTestsRunner,
            int alpha,
            int beta,
            int version,
            int secondaryVersion,
            int tertiaryVersion,
            String projectGroup,
            String projectPackage,
            boolean proguard
    ) {
        super.setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage)
        project.android {
            publishNonDefault true
            compileSdkVersion androidSdkVersion
            buildToolsVersion androidBuildToolsVersion
            defaultConfig {
                minSdkVersion minSdk
                targetSdkVersion targetSdk
                testInstrumentationRunner instrumentationTestsRunner
            }
            sourceSets.main.java.srcDirs = [
                    'src/main/java',
                    'src/common/java'
            ]
            buildTypes {
                release {
                    minifyEnabled proguard
                    shrinkResources false
                    if (proguard) {
                        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
                    }
                }
                debug {
                    minifyEnabled false
                    shrinkResources false
                }
            }
        }
        project.dependencies {
            compile project.fileTree(dir: 'libs', include: '*.jar')
        }
        setupBuildDestination()
        setupModuleRequire()
        setupReleaseCopy()
    }

    void setupFlavor(String flavor) {
        if (!isProjectSetup) {
            throw new IllegalStateException("groot.android.project.setup( ... ) method not called. Must setup project before set flavors.")
        }
        project.android.productFlavors {
            "${flavor}" {
                buildConfigField 'String', 'VARIANT', "\"$projectBuildVariant\""
            }
        }
        project.android.sourceSets."$flavor".java {
            srcDirs = [
                    "src/${flavor}/java",
                    "src/common/java"
            ]
        }
        setupReleaseCopy(flavor)
    }

    void setupBuildVariant(String variant) {
        setupBuildVariant(variant, false)
    }

    void setupBuildVariant(String variant, boolean proguard) {
        if (!isProjectSetup) {
            throw new IllegalStateException("groot.android.project.setup( ... ) method not called. Must setup project before set flavors.")
        }
        def bVariant = project.android.buildTypes.create(variant)
        bVariant.shrinkResources(false)
        if (proguard) {
            bVariant.minifyEnabled(true)
            bVariant.proguardFiles(
                    project.android.getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            )
        } else {
            bVariant.minifyEnabled(false)
        }
    }

    void sign(Map jksParams) {
        File jks = new File("${jksParams["jksFile"]}")
        def sConfig = project.android.signingConfigs.create("signingConfig", {
            storeFile jks
            storePassword jksParams["storePassword"]
            keyAlias jksParams["keyAlias"]
            keyPassword jksParams["keyPassword"]
        })
        project.android.buildTypes.release.signingConfig sConfig
    }

    private void setupBuildDestination() {
        if (project.android.hasProperty("libraryVariants")) {
            project.android {
                libraryVariants.all {
                    variant ->
                        variant.outputs.each {
                            output ->
                                String fileName = new StringBuilder()
                                        .append(output.outputFile.parent)
                                        .append(File.separator)
                                        .append(variant.name)
                                        .append(File.separator)
                                        .append(getFilename("aar"))
                                        .toString()
                                output.outputFile = new File(fileName)
                        }
                }
            }
        }
        if (project.android.hasProperty("applicationVariants")) {
            project.android {
                applicationVariants.all {
                    variant ->
                        variant.outputs.each {
                            output ->
                                String fileName = new StringBuilder()
                                        .append(output.outputFile.parent)
                                        .append(File.separator)
                                        .append(variant.name)
                                        .append(File.separator)
                                        .append(getFilename("apk"))
                                        .toString()
                                output.outputFile = new File(fileName)
                        }
                }
            }
        }
    }

    private String getFilename(String extension) {
        return "${project.name}_${projectVersion}.$extension"
    }

    private void setupModuleRequire() {
        project.dependencies {
            ext.requireModule = {
                module ->
                    if (project.android.productFlavors.size() > 0) {
                        project.android.productFlavors.each {
                            flavor ->
                                project.android.buildTypes.each {
                                    buildType ->
                                        String capitalized = "${buildType.name}".substring(0, 1).toUpperCase()
                                        capitalized += "${buildType.name}".substring(1, "${buildType.name}".length())
                                        String variantName = "${flavor.name}$capitalized"
                                        project.dependencies {
                                            compile project.dependencies.project(path: module, configuration: "$variantName")
                                        }
                                }
                        }
                    } else {
                        project.android.buildTypes.each {
                            buildType ->
                                String variantName = "${buildType.name}"
                                project.dependencies {
                                    compile project.dependencies.project(path: module, configuration: "$variantName")
                                }
                        }
                    }
            }
        }
    }

    private void setupReleaseCopy(String flavor) {
        String variantName = "${flavor}Release"
        String taskName = "copyAndroidRelease_$variantName"
        project.task([type: Copy], taskName, {
            if (projectBuildVariant == "RELEASE") {
                from "build${File.separator}outputs${File.separator}aar${File.separator}${variantName}"
                into "Releases"
            }
        })
        project.copyRelease.finalizedBy(taskName)
    }

    private void setupReleaseCopy() {
        String taskName = "copyAndroidRelease"
        project.task([type: Copy], taskName, {
            if (projectBuildVariant == "RELEASE") {
                from "build${File.separator}outputs${File.separator}aar${File.separator}release"
                into "Releases"
            }
        })
        project.copyRelease.finalizedBy(taskName)
    }

}
