package net.milosvasic.tryout.groot.scala


object Main {
  def main(args: Array[String]): Unit = {
    println("Hello, Scala!")
    println(BuildConfig.NAME)
    println(BuildConfig.VERSION)
    val computer = new Computer(4, 4 * 1024 * 1024)
    println(computer)
  }
}