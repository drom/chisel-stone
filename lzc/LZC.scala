import chisel3._
// import chisel3.util._

class LZC(w: Int) extends Module {
  val io = IO(new Bundle {
    val inp = Input(Vec(w, Bool()))
    val oup = Output(UInt(8.W))
  })
  io.oup := 255.U
  for (i <- (w - 1) to 0 by -1) {
    when (io.inp(i)) {
      io.oup := i.U
    }
  }
}

object LZC extends App {
  chisel3.Driver.execute(args, () => new LZC(args(0).toInt))
}
