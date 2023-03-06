package stuff.logic;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.*;

import static stuff.logic.Array.*;

import java.util.Hashtable;

import static stuff.logic.AFunc.TwoType;

public class TheInstruction{
    public Hashtable<String, Array> storage = new Hashtable<String, Array>();

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

    public static class AFunction implements LInstruction{
        public AFunc OpA = AFunc.New;
        public TwoType TT = TwoType.number;
        public int a, b, c, d, e, result;
        public String A, B, Result;

        public AFunction(AFunc OpA, TwoType TT, int a, int b, int c, int d, int e, int result, String A, String B, String Result){
            this.OpA = OpA;
            this.TT = TT;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.result = result;
            this.A = A;
            this.B = B;
            this.Result = Result;
        }

        AFunction(){}

        @Override
        public void run(LExecutor exec){
            Array arr1 = new Array(1, 1, 1),
                  arr2 = new Array(1, 1, 1);
            TheInstruction Inst = new TheInstruction();
            try{arr1 = Inst.storage.get(A);}catch(Exception e){}
            try{arr2 = Inst.storage.get(B);}catch(Exception e){}
            double s0 = exec.num(b);
            double s_1 = exec.num(c);
            double s = exec.num(e);
            boolean b1 = exec.bool(e);
            int s2 = exec.numi(b);
            int[] s3 = {s2, exec.numi(c), exec.numi(d)};
            try{
                switch(OpA){
                    case New -> {
                        Inst.storage.put(Result, new Array(exec.numi(a), s2, exec.numi(c)));
                        break;
                    }
                    case Add -> {
                        arr1.add(arr2);
                        exec.setobj(result, arr1.toString());
                        break;
                    }
                    case Subtract -> {
                        arr1.minus(arr2);
                        exec.setobj(result, arr1.toString());
                        break;
                    }
                    case Muliply -> {
                        switch(TT){
                            case array -> {
                                arr1.prodEach(arr2);
                                exec.setobj(result, arr1.toString());
                            }
                            case number -> {
                                arr1.prod(s0);
                                exec.setobj(result, arr1.toString());
                            }
                        }
                        break;
                    }
                    case Divide -> {
                        switch(TT){
                            case array -> {
                                arr1.divEach(arr2);
                                exec.setobj(result, arr1.toString());
                            }
                            case number -> {
                                arr1.div(s0);
                                exec.setobj(result, arr1.toString());
                            }
                        }
                        break;
                    }
                    case SumAll-> {
                        exec.setnum(result, arr1.sumAll());
                        break;
                    }
                    case Change -> {
                        switch(TT){
                            case array -> {
                                arr1.Change(s3, s);
                                exec.setobj(a, arr1.toString());
                            }
                            case number -> {
                                arr1.Change(s2, s_1);
                                exec.setobj(a, arr1.toString());
                            }
                        }
                        break;
                    }
                    case CrossProduct -> {
                        exec.setobj(result, arr1.crossProd(arr2).toString());
                        break;
                    }
                    case DotProd -> {
                        exec.setnum(result, arr1.dotProd(arr2));
                        break;
                    }
                    case Get -> {
                        switch(TT){
                            case array -> exec.setnum(result, arr1.getNum(s3));
                            case number -> exec.setnum(result, arr1.getNum(s2));
                        }
                        break;
                    }
                    case ProductAll -> {
                        exec.setnum(result, productAll(arr1.s));
                        break;
                    }
                    case Resize -> {
                        arr1.Resize(s3, b1);
                        exec.setobj(result, arr1.toString());
                        break;
                    }
                    case Shuffle -> {
                        arr1.shuffle();
                        exec.setobj(a, arr1.toString());
                        break;
                    }
                }
            }catch(Exception n){
                if(!OpA.local) exec.setobj(result, null);
            }
        }
    }
}
