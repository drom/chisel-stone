import chisel3._
import scala.math._
// import chisel3.util._

class LZCgen(w: Int) extends Module {

  def log2max(x: Double): Int = ceil(log10(x) / log10(2.0)).toInt

  val log2w = log2max(w);

  val io = IO(new Bundle {
    val inp = Input(Vec(w, Bool()))
    val oup = Output(Vec(w, Bool()))
    // val oup = Output(UInt(log2w.W))
  })

  val tmp = io.inp.map { case a => a ^ true.B}
  // Seq(true.B, false.B).foldLeft(io.inp) {
  //   case (vec, b) => vec.map { case a => a ^ b }
  // }
  // var x = io.inp
  // for(b <- Seq(true.B, false.B)) {
  //   x = x.map { case a => a ^ b }
  // }

  io.oup := tmp
  // io.oup := 1.U // -1 ???
  //
  // for (i <- (w - 1) to 0 by -1) {
  //   when (io.inp(i)) {
  //     io.oup := i.U
  //   }
  // }


}

object LZCgen {
  def apply(input: Vec[Bool]): Vec[Bool] = {
    val mod = Module(new LZCgen(input.length))
    mod.io.inp := input
    mod.io.oup
  }
}

class LZC(w: Int) extends Module {

  def log2max(x: Double): Int = ceil(log10(x) / log10(2.0)).toInt

  val log2w = log2max(w);

  val io = IO(new Bundle {
    val inp = Input(Vec(w, Bool()))
    val oup = Output(Vec(w, Bool()))
    // val oup = Output(UInt(log2w.W))
  })

  io.oup := LZCgen(io.inp)
}

object LZC extends App {
  //chisel3.Driver.execute(args, () => new LZC(args(0).toInt))
  println(chisel3.Driver.emitVerilog(new LZC(args(0).toInt)))
}
