# Groot, Gradle's best friend!
So, what is Groot actually?
Groot is set of Gradle plugins. It will allow you easily to setup your project for building and it's maven deployment configuration.

# What plugins does it have?
Groot comes with the following plugins:
- groot
- groot-kotlin
- groot-credentials

# How to use it?
To use Groot first you have to add it to your dependencies.
Then, depending of your needs you setup your build gradle configuration for your current module.
Module may be application module or library module.

To use Groot follow this example:

- Apply Groot plugins:
```
apply plugin: "groot"
apply plugin: "groot-kotlin"
apply plugin: "groot-credentials"
```
- If you have Maven repositories on which you already published some of your dependencies you can register it to Groot:
```
groot.registerRepository("http://repo.milosvasic.net/releases")
groot.registerRepository("http://repo.milosvasic.net/development")
```
- Register dependencies that exist on these repositories: 
```
groot.depend("net.milosvasic.logger", "Logger", "1.2.0")
groot.depend("net.milosvasic.factory", "ProjectFactory", "1.0.0_Alpha_1_DEV_+")
```
- Configure your module:
```
final alpha = 1
final beta = 0
final version = 1
final secondaryVersion = 0
final tertiaryVersion = 0
final projectPackage = "groot"
final projectGroup = "net.milosvasic.tryout"

groot.kotlin.project.setup(
        alpha,
        beta,
        version,
        secondaryVersion,
        tertiaryVersion,
        projectGroup,
        projectPackage
)
```
- If your project is application setup it too:
```
String fullPackage = groot.kotlin.project.projectPackage
String fullVersion = groot.kotlin.project.projectVersion

groot.kotlin.application.setup(fullPackage)
```
- To deploy your project it is needed to provide access to FTP server where builds will be released: 
```
groot.deployment.ftp.host = ftpServer
groot.deployment.ftp.username = ftpUsername
groot.deployment.ftp.password = ftpPassword
groot.deployment.setup(fullPackage, fullVersion)
```
Plugin groot-credentials will create credentials files for your release configurations.
These credentials files should be git ignored.
Groot expect to find them in your module directory. If they do not exist default ones will be created.
For example, you may have the following credentials files:
```
credentials.gradle
credentials_development.gradle
credentials_production.gradle
credentials_something_else.gradle
```

# How to build and publish?
Standard building with publishing will be done like this:
```
./gradlew clean
./gradlew assemble
```
Groot will look for credentials.gradle file to perform FTP upload to your Maven repository.
If we try this:
```
./gradlew -Pdeploy=production assemble
```
Groot will look for credentials_production.gradle and read FTP account information.

# Build variants
Default Groot build variant is DEV. Production variant is RELEASE.
Every time we build new DEV released is created and published using your publishing configuration defined in credentials.gradle.

If your other project expects latest DEV release you can depend on it like in this example:
```
groot.registerRepository("http://repo.milosvasic.net/development")
groot.depend("net.milosvasic.factory", "ProjectFactory", "1.0.0_Alpha_1_DEV_+")
```

To build other variant do this:
```
./gradlew -Pvariant=RELEASE assemble
```
or something like:
```
./gradlew -Pvariant=STAGING assemble
```
And finally, let's say you wish to build PRODUCTION variant and release it to production Maven repository:
```
./gradlew -Pvariant=RELEASE -Pdeploy=production assemble
```
