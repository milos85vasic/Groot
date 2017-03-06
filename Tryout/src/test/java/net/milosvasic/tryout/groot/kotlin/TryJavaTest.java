package net.milosvasic.tryout.groot.kotlin;


import org.junit.Assert;
import org.junit.Test;

public class TryJavaTest {

    @Test
    public void testJava(){
        Car car = new Car("BMW", "Gasoline");
        Assert.assertEquals("BMW", car.getBrand());
    }

}
