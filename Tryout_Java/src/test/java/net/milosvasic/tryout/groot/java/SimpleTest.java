package net.milosvasic.tryout.groot.java;


import org.junit.Test;

public class SimpleTest {

    @Test
    public void testSimple() {
        Person person = new Person("John", "Smith", 1970);
        assert person.getYearOfBirth() > 0;
    }

}
