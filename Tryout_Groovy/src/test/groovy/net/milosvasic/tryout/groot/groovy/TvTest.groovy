package net.milosvasic.tryout.groot.groovy

import org.junit.Test


class TvTest {

    private class Sony extends Tv {
        Sony() {
            super("Sony")
        }
    }

    @Test
    void testTv(){
        Tv tv = new Sony()
        assert "Sony" == tv.getBrand()
        tv = new Panasonic()
        assert "Panasonic" == tv.getBrand()
    }

}
