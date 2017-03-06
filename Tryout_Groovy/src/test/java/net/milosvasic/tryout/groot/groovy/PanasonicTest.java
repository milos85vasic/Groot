package net.milosvasic.tryout.groot.groovy;

import net.milosvasic.tryout.groot.groovy.Tv;
import org.junit.Test;

public class PanasonicTest {

    @Test
    public void testPanasonic() {
        Tv panasonic = new Panasonic();
        assert panasonic.getBrand().equals("Panasonic");
    }

}
