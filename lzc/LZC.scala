import chisel3._
import util._

class LZCgen(target: Vec[Bool]) extends Module {
  val w = target.length
  val log2w = log2max(w)

  val brick = (a: Vec[Bool], b: Vec[Bool]) => {
    val len = a.length
    val res = Wire(Vec(len + 1, Bool()))
    for (i <- 0 to len - 1) {
      res(i) := (if (i == 0) a(i) | b(i) else (~b(0) & a(i)) | b(i))
    }
    res(len) := b(0)
    res
  }

  val io = IO(new Bundle {
    val inp = Input(target)
    val oup = Output(Vec(log2w + 1, Bool()))
  })

  var i = 0
  val rec: Int => Vec[Bool] = (n: Int) => {
    if (n == 0) {
      i += 1
      VecInit(io.inp(i - 1))
    } else {
      brick(rec(n - 1), rec(n - 1))
    }
  }
  io.oup := rec(log2w)
}

object LZCgen {
  def apply(input: Vec[Bool]): Vec[Bool] = {
    val mod = Module(new LZCgen(chisel3.core.chiselTypeOf(input)))
    mod.io.inp := input
    mod.io.oup
  }
}

class LZC(w: Int) extends Module {
  val log2w = log2max(w)
  val io = IO(new Bundle {
    val inp = Input(Vec(w, Bool()))
    val oup = Output(Vec(log2w + 1, Bool()))
  })
  io.oup := LZCgen(io.inp)
}

object LZC extends App {
  println(chisel3.Driver.emitVerilog(new LZC(args(0).toInt)))
}
