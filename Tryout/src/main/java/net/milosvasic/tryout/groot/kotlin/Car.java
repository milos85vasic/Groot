package net.milosvasic.tryout.groot.kotlin;

public class Car {

    private String fuel;
    private String brand;

    public Car(String brand, String fuel) {
        this.brand = brand;
        this.fuel = fuel;
    }

    public String getFuel() {
        return fuel;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String toString() {
        return "Car{" +
                "fuel='" + fuel + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

}
