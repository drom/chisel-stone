import chisel3._
// import chisel3.util._

class LZC(w: Int) extends Module {
  val io = IO(new Bundle {
    val inp = Input(UInt(w.W))
    val oup = Output(UInt(w.W))
  })
  io.oup := io.inp
}

object LZC extends App {
  chisel3.Driver.execute(args, () => new LZC(args(0).toInt))
}

