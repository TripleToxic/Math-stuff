package stuff.logic;

import mindustry.logic.LExecutor;

public class Operations extends LExecutor{
    
    public void result1(int outputVar, double input){
        Var v1 = var(outputVar);
        v1.numval = input;
    }

    public void result2(int outputVar1, int OutputVar2, double input1, double input2){
        Var v1 = var(outputVar1);
        Var v2 = var(outputVar1);
        Var v3 = var(OutputVar2);
        Var v4 = var(OutputVar2);
        v1.numval = input1;
        v2.numval = input2;
        v3.numval = input1;
        v4.numval = input2;
    }

    public interface Run{
        void run(Operations exec);
    }

    public static class Function implements Run, LInstruction{
        public Func Op = Func.add;
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
        public void run(Operations exec){
            if (Op.SingleInputCheck){
                if (Op.SingleOutputCheck){
                    exec.result1(RealOutput, Op.SingleOutput.get(exec.num(r1), exec.num(i1)));
                }
                else{
                    exec.result2(RealOutput, ImaginaryOutput, Op.Func2.get(exec.num(r1), exec.num(i1)), Op.Func3.get(exec.num(r1), exec.num(i1)));
                }
            }
            else{
                exec.result2(RealOutput, ImaginaryOutput, Op.Func4.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)), Op.Func5.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)));
            }
        }

        @Override
        public void run(LExecutor exec) {
            
        }  
    }
    
}