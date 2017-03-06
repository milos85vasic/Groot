package net.milosvasic.tryout.groot.groovy


class Main {

    static void main(String[] args) {
        Animal animal = new Animal("Elephant", 4)
        println(animal)
        println "- - - - - - -"
        println("${BuildConfig.NAME} ${BuildConfig.VERSION}")
    }

}
