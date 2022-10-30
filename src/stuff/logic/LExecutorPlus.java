package stuff.logic;

import mindustry.logic.LExecutor;

import static mindustry.Vars.*;

public class LExecutorPlus extends LExecutor {

    public static boolean invalid(double d){
        return Double.isNaN(d) || Double.isInfinite(d);
    }

    public Var[] arr(int[] arr){
        int L = arr.length;
        Var[] v = new Var[L];
        for(int i=0; i<L; i++){
            v[i] = arr[i] < 0 ? logicVars.get(-arr[i]) : vars[arr[i]];
        }
        return v;
    }

    public double[] vect(int[] index){
        int count = index.length;
        double[] arr = new double[count];
        for(int i=0; i<count; i++){
            Var v = var(index[i]);
            double d = v.isobj ? v.objval != null ? 1 : 0 : invalid(v.numval) ? 0 : v.numval;
            arr[i] = d;
        }
        return arr;
    }

    public void setvect(int[] index, double[] vect){
        int count = index.length;
        for(int i=0; i<count; i++){
            for(int k=0; k<1; k++){
                Var v = var(index[i]);
                if(v.constant) break;
                if(invalid(vect[i])){
                    v.objval = null;
                    v.isobj = true;
                }else{
                    v.numval = vect[i];
                    v.objval = null;
                    v.isobj = false;
                } 
            }
        }
    }

    public interface LInstructionPlus{
        void run(LExecutorPlus exec);
    }

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

    public static class SetArray implements LInstructionPlus{
        public int[] from, to;

        public SetArray(int[] to, int[] from){
            this.from = from;
            this.to = to;
        }

        SetArray(){}

        @Override
        public void run(LExecutorPlus exec) {
            Var[] T = exec.arr(to);
            Var[] F = exec.arr(from);

            for(int i=0; i<F.length; i++){
                if(!T[i].constant){
                    if(F[i].isobj){
                        T[i].objval = F[i].objval;
                        T[i].isobj = true;
                    }else{
                        T[i].numval = invalid(F[i].numval) ? 0:F[i].numval;
                        T[i].isobj = false;
                    }
                }
            }
        }
    }

    public static class VFunction implements LInstructionPlus{
        public VFunc Opv = VFunc.addV;
        public int scalar;
        public int[] a, b, result;

        public VFunction(VFunc Opv, int[] a, int[] b, int[] result, int scalar){
            this.Opv = Opv;
            this.a = a;
            this.b = b;
            this.result = result;
            this.scalar = scalar;
        }

        VFunction(){}

        @Override
        public void run(LExecutorPlus exec){
            if(Opv.scalar){
                exec.setnum(scalar, Opv.op1.get(exec.vect(a), exec.vect(b)));
            }else if(Opv.cross){
                exec.setvect(result, Opv.op3.get(exec.vect(a), exec.vect(b)));
            }else{
                exec.setvect(result, Opv.op2.get(exec.vect(a), exec.vect(b)));
            }
        }
    }
}
