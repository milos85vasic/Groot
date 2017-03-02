package net.milosvasic.`try`.groot

fun main(args: Array<String>) {

    val items = mutableListOf("Sss", "Zzz")
    items.add("Ccc")
    println("Trying out Groot.")
    items.forEachIndexed { i, s -> println(">>>>> $s") }

}
