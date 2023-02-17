package stuff.logic;
import mindustry.logic.LExecutor;

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
            if(Op.isRealFunction) exec.setnum(RealOutput, Op.RealFunction.get(exec.num(r1)));
            if (Op.SingleInputCheck){
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
        public AFunc Opv = AFunc.Add;
        public int a, b, c, d, n, result;

        public VFunction(AFunc Opv, int a, int b, int c, int d, int n, int result){
            this.Opv = Opv;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.n = n;
            this.result = result;
        }

        VFunction(){}

        @Override
        public void run(LExecutor exec){
            Var a1 = exec.var(a);
            Var a2 = exec.var(b);
            ArrayStringDouble arr1 = a1.isobj && a1.objval instanceof ArrayStringDouble arr1B ? arr1B : null;
            ArrayStringDouble arr2 = a2.isobj && a2.objval instanceof ArrayStringDouble arr2B ? arr2B : null;
            double s = exec.num(c);
            int s2 = exec.numi(d);
            boolean B = exec.bool(n);
            switch(Opv){
                case Add -> {
                    arr1.add(arr2);
                    exec.setobj(B ? a : result, arr1);
                }
                case Subtract -> {
                    arr1.minus(arr2);
                    exec.setobj(B ? a : result, arr1);
                }
                case Muliply -> {
                    arr1.prodEach(arr2);
                    exec.setobj(B ? a : result, arr1);
                }
                case Divide -> {
                    arr1.divEach(arr2);
                    exec.setobj(B ? a : result, arr1);
                }
                case ScalarMul -> {
                    arr1.prod(s);
                    exec.setobj(B ? a : result, arr1);
                }
                case ScalarDiv -> {
                    arr1.div(s);
                    exec.setobj(B ? a : result, arr1);
                }
                case SumAll-> {
                    exec.setnum(result, arr1.sumAll());
                }
                case ChangeInt -> {
                    arr1.Change(d, s);
                    exec.setobj(a, arr1);
                }
                case CrossProduct -> {

                }
                case DotProd -> {

                }
                case Get -> {

                }
                case ProductAll -> {

                }
                case Resize -> {
                    exec.setobj(B ? a : result, arr1);
                }
                case Shuffle -> {

                }
            }
        }
    }
}
