package net.milosvasic.tryout.groot.scala;

import org.junit.Assert;
import org.junit.Test;

public class ComputerTest {

    @Test
    public void testComputer() {
        Computer mac = new Computer(4, 4 * 1024 * 1024);
        Assert.assertTrue(mac.getCpus() == 4);
    }

}
