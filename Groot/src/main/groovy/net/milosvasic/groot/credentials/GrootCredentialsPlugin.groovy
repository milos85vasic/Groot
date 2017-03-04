package net.milosvasic.groot.credentials

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootCredentialsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        String destination
        try {
            destination = project.deploy
        } catch (Exception e) {
            // Ignore
        }
        File credentialsFile = new File(project.projectDir, "credentials.gradle")
        if (destination != null && destination.length() > 0) {
            credentialsFile = new File(project.projectDir, "credentials_${destination}.gradle")
        }
        if (!credentialsFile.exists()) {
            println("Credentials file does not exist. Creating default one.")
            String defaultCredentials = new StringBuilder()
                    .append("/**\n")
                    .append("* FTP server\n")
                    .append("*/\n")
                    .append("ext.ftpServer = \"repo.example.com\"\n\n")
                    .append("/**\n")
                    .append("* FTP username\n")
                    .append("*/\n")
                    .append("ext.ftpUsername = \"your_username\"\n\n")
                    .append("/**\n")
                    .append("* FTP password\n")
                    .append("*/\n")
                    .append("ext.ftpPassword = \"yout_password\"")
                    .toString()
            def srcStream = new ByteArrayInputStream(defaultCredentials.bytes)
            def dstStream = credentialsFile.newDataOutputStream()
            dstStream << srcStream
            srcStream.close()
            dstStream.close()
        } else {
            println("Credentials file exist.")
        }
        project.apply(from: credentialsFile.absolutePath)
    }

}
