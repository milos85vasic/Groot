package net.milosvasic.groot

import org.gradle.api.Project
import org.gradle.api.Plugin

class Groot : Plugin<Project> {

    override fun apply(target: Project) {
        println("Groot says hello from [ net.milosvasic.groot ]!")
    }

}