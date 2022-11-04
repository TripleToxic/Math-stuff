package stuff.logic;

import mindustry.logic.LExecutor;
import static stuff.AdditionalFunction.*;

public class TheInstruction extends LExecutor{

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

    public static class VFunction implements LInstruction{
        static LExecutor exec = new LExecutor();
        public VFunc Opv = VFunc.addV;
        public int a, b, result;

        public VFunction(VFunc Opv, int a, int b, int result){
            this.Opv = Opv;
            this.a = a;
            this.b = b;
            this.result = result;
        }

        VFunction(){}

        @Override
        public void run(LExecutor exec){
            if(exec.obj(a) instanceof String astr  &&  exec.obj(b) instanceof String bstr){
                switch(Opv){
                    case addV -> exec.setobj(result, ArrToString(AddVector(vect(StringToArr(astr)), vect(StringToArr(bstr)))));
                    case subV -> exec.setobj(result, ArrToString(SubVector(vect(StringToArr(astr)), vect(StringToArr(bstr)))));
                    case dotV -> exec.setnum(result, DotProc(vect(StringToArr(astr)), vect(StringToArr(bstr))));
                    case crossV -> exec.setobj(result, ArrToString(CrossProc(vect(StringToArr(astr)), vect(StringToArr(bstr)))));
                }
            }else{exec.setobj(result, null);}  
        }

        public static double[] vect(int[] index){
            int count = index.length;
            double[] arr = new double[count];
            for(int i=0; i<count; i++){
                arr[i] = exec.num(index[i]);
            }
            return arr;
        }
    }
}