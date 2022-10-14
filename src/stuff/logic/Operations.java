package stuff.logic;

import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.*;
import stuff.logic.LExecutorPlus.LInstructionPlus;

public class Operations{

    public static class Function implements LInstruction{
        public Func Op = Func.addC;
        public int r1, r2, i1, i2, RealOutput, ImaginaryOutput;

        public Function(Func Op, int r1, int i1, int r2, int i2, int RealOutput, int ImaginaryOutput){
            this.Op = Op;
            this.r1 = r1;
            this.r2 = r2;
            this.i1 = i1;
            this.i2 = i2;
            this.RealOutput = RealOutput;
            this.ImaginaryOutput = ImaginaryOutput;
        }

        Function(){}

        @Override
        public void run(LExecutor exec){
            if (Op.isConstant){
                exec.setnum(RealOutput, Op.Constants.get(exec.num(r1)));
            }else if (Op.SingleInputCheck){
                if (Op.SingleOutputCheck){
                    exec.setnum(RealOutput, Op.SingleOutput.get(exec.num(r1), exec.num(i1)));
                }else{
                    exec.setnum(RealOutput, Op.Func2.get(exec.num(r1), exec.num(i1)));
                    exec.setnum(ImaginaryOutput, Op.Func3.get(exec.num(r1), exec.num(i1)));
                }
            }else{
                exec.setnum(RealOutput, Op.Func4.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)));
                exec.setnum(ImaginaryOutput, Op.Func5.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)));
            }
        }
    }

    public static class VFunction implements LInstructionPlus{
        public VFunc Opv = VFunc.addV;
        public int[] a, b, result;
        public int scalar, n;

        public VFunction(VFunc Opv, int[] a, int[] b, int[] result, int scalar, int n){
            this.Opv = Opv;
            this.a = a;
            this.b = b;
            this.result = result;
            this.scalar = scalar;
            this.n = n;
        }

        VFunction(){}

        @Override
        public void run(LExecutorPlus exec){
            if(Opv.scalar == true){
                exec.setnum(scalar, Opv.op1.get(exec.vect(a), exec.vect(b)));
            }else{
                exec.setvect(result, Opv.op2.get(exec.vect(a), exec.vect(b)));
            }
        }
    }
}
