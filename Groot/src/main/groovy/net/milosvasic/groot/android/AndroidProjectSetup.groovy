package net.milosvasic.groot.android

import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project

class AndroidProjectSetup extends ProjectSetup {

    AndroidProjectSetup(Project project) {
        super(project)
        generateBuildConfig = false
    }

    @Override
    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage) {
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
                    minifyEnabled false
                    shrinkResources false
                    proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
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
            String projectPackage
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
                    minifyEnabled false
                    shrinkResources false
                    proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
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
    }

    void setupFlavor(String flavor) {
        project.android.productFlavors.create(flavor, {})
        project.android.sourceSets[flavor].java.srcDirs =
                [
                        "src/$flavor/java",
                        "src/common/java"
                ]
    }

    void setupBuildVariant(String variant) {
        setupBuildVariant(variant, false)
    }

    void setupBuildVariant(String variant, boolean proguard) {
        def bVariant = project.android.buildTypes.create(variant)
        bVariant.minifyEnabled(false)
        bVariant.shrinkResources(false)
        if (proguard) {
            bVariant.proguardFiles(
                    project.android.getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            )
        }
    }

    private void setupBuildDestination() {
        if (project.android.hasProperty("libraryVariants")) {
            project.android {
                libraryVariants.all {
                    variant ->
                        variant.outputs.each {
                            output ->
                                output.outputFile = new File(
                                        "${output.outputFile.parent}${File.separator}${getFilename("${output.outputFile.name}", "aar")}"
                                )
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
                                output.outputFile = new File(
                                        "${output.outputFile.parent}${File.separator}${getFilename("${output.outputFile.name}", "apk")}"
                                )
                        }
                }
            }
        }
    }

    private String getFilename(String name, String extension) {
        String finalName = name.replace(".$extension", "")
        finalName += "_${projectVersion}.$extension"
        return finalName
    }

}
