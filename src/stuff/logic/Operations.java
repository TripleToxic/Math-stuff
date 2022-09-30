package stuff.logic;

import mindustry.logic.LExecutor;

public class Operations extends LExecutor{

    public static boolean invalid(double v){
        return Double.isNaN(v) || Double.isInfinite(v);
    }

    public void result2(int outputVar1, int OutputVar2, double input1, double input2){
        Var v1 = var(outputVar1);
        Var v2 = var(outputVar1);
        Var v3 = var(OutputVar2);
        Var v4 = var(OutputVar2);
        if(v1.constant)return;
        if(v2.constant)return;
        if(v3.constant)return;
        if(v4.constant)return;
        if(invalid(input1)){
            v1.objval = null;
            v3.objval = null;
        }else{
            v1.numval = input1;
            v3.numval = input1;
            v1.objval = null;
            v3.objval = null;
        }
        if(invalid(input2)){
            v2.objval = null;
            v4.objval = null;
        }else{
            v2.numval = input2;
            v4.numval = input2;
            v2.objval = null;
            v4.objval = null;
        }
    }

    public interface Run{
        void run(Operations exec, LExecutor execute);
    }

    public static class Function implements Run{
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
        public void run(Operations exec, LExecutor execute){
            if (Op.SingleInputCheck){
                if (Op.SingleOutputCheck){
                    execute.setnum(RealOutput, Op.SingleOutput.get(execute.num(r1), execute.num(i1)));
                }else{
                    exec.result2(RealOutput, ImaginaryOutput, Op.Func2.get(execute.num(r1), execute.num(i1)), Op.Func3.get(execute.num(r1), execute.num(i1)));
                }
            }else{
                exec.result2(RealOutput, ImaginaryOutput, Op.Func4.get(execute.num(r1), execute.num(i1), execute.num(r2), execute.num(i2)), Op.Func5.get(execute.num(r1), execute.num(i1), execute.num(r2), execute.num(i2)));
            }
        }
    }  
}
