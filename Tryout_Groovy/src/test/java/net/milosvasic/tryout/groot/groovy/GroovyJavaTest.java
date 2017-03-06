package net.milosvasic.tryout.groot.groovy;


import org.junit.Test;

public class GroovyJavaTest {

    @Test
    public void testGroovy() {
        String label = Tv.class.getSimpleName();
        assert label.equals("Tv");
    }

}
