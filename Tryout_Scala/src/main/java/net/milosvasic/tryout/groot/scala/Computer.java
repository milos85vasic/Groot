package net.milosvasic.tryout.groot.scala;


public class Computer {

    private int cpus;
    private long ram;

    public Computer(int cpus, long ram) {
        this.cpus = cpus;
        this.ram = ram;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "cpus=" + cpus +
                ", ram=" + ram +
                '}';
    }

}
