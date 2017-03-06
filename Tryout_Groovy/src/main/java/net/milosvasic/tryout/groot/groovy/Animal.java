package net.milosvasic.tryout.groot.groovy;


public class Animal {

    private String name;
    private int legsCount;

    public Animal(String name, int legsCount) {
        this.name = name;
        this.legsCount = legsCount;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", legsCount='" + legsCount + '\'' +
                '}';
    }

}
