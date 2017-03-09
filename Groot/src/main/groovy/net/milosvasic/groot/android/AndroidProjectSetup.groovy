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
            sourceSets.main.java.srcDirs += 'src/main/java'
            sourceSets.main.java.srcDirs += 'src/common/java'
            sourceSets.test.java.srcDirs += 'src/test/java'
        }
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
            sourceSets.main.java.srcDirs += 'src/main/java'
            sourceSets.main.java.srcDirs += 'src/common/java'
            sourceSets.test.java.srcDirs += 'src/test/java'
        }
    }

    void setupFlavor(String flavor) {
        project.android.productFlavors.create(flavor, {})
    }

}
