package net.milosvasic.tryout.groot.groovy;


abstract public class Tv {

    private String brand;

    Tv(String brand) {
        this.brand = brand;
    }

    String getBrand() {
        return brand;
    }

}
