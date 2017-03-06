package net.milosvasic.tryout.groot.kotlin

import net.milosvasic.tryout.groot.kotlin.BuildConfig
import org.junit.Assert
import org.junit.Test

class TryKotlinTest {

    @Test
    fun testKotlin(){
        Assert.assertTrue(BuildConfig.NAME.startsWith("Tryout"))
    }

}