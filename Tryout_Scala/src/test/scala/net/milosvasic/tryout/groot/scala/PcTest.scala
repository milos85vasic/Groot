package net.milosvasic.tryout.groot.scala


import org.scalatest.{BeforeAndAfter, FunSuite}

class PcTest extends FunSuite with BeforeAndAfter {

  var pc: Pc = _

  before {
    pc = new Pc()
  }

  test("pc has 4 cpus") {
    assert(pc.getCpus === 4)
  }

}