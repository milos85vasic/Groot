package net.milosvasic.tryout.groot

import net.milosvasic.factory.project.ProjectFactory
import net.milosvasic.logger.ConsoleLogger

private class Factory : ProjectFactory()

fun main(args: Array<String>) {

    val TAG = Factory::class
    val logger = ConsoleLogger()

    val items = mutableListOf("Lion", "Elephant")
    items.add("Cow")
    println("Trying out Groot.")
    items.forEachIndexed { i, s -> logger.c(TAG, s) }
    println("${BuildConfig.NAME} ${BuildConfig.VERSION}")

}
