package src;

public class Operations {
    public Var[] vars = {};
    public Var var(int outputVar){
        return vars[outputVar];
    }

    private static boolean invalid(double i){
        return Double.isNaN(i) || Double.isInfinite(i);
    }

    public double num(int outputVar){
        Var v = var(outputVar);
        return  invalid(v.value) ? 0 : v.value;
    }

    public void result1(int outputVar, double input){
        Var v1 = var(outputVar);
        v1.value = input;
    }

    public void result2(int outputVar1, int OutputVar2, double input1, double input2){
        Var v1 = var(outputVar1);
        Var v2 = var(outputVar1);
        Var v3 = var(OutputVar2);
        Var v4 = var(OutputVar2);
        v1.value = input1;
        v2.value = input2;
        v3.value = input1;
        v4.value = input2;
    }

    public static class Var{
        public final String name;
        
        public boolean Bool;
        
        public double value;

        public Var(String name){
            this.name = name;
        }
    }
    public interface Run{
        void run(Operations exec);
    }
    public static class Function implements Run {
        public Func Op = Func.add;
        public int r1, r2, i1, i2, RealOutput, ImaginaryOutput;

        public Function(Func Op, int r1, int r2, int i1, int i2, int RealOutput, int ImaginaryOutput){
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
    }

}
