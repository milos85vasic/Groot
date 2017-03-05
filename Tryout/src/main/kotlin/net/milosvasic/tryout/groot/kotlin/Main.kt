package net.milosvasic.tryout.groot.kotlin

import net.milosvasic.factory.project.ProjectFactory
import net.milosvasic.logger.ConsoleLogger

private class Factory : ProjectFactory()

fun main(args: Array<String>) {

    println("Trying out Groot.")
    println("${BuildConfig.NAME} ${BuildConfig.VERSION}")

    val TAG = Factory::class
    val logger = ConsoleLogger()

    val items = mutableListOf("Lion", "Elephant")
    items.add("Cow")
    items.forEachIndexed { i, s -> logger.c(TAG, s) }

    val car = Car("Fiat", "Benzin")
    logger.n(TAG, car.toString())

}
