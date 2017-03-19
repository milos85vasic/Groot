package net.milosvasic.groot.languages


interface Language {

    String getBuildConfigClassFilename()

    String getBuildConfigClassContent(String projectPackage, String projectVersion, String projectName, String buildVariant)

    String getMainClassName()

}