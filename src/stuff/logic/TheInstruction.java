package stuff.logic;
import mindustry.logic.LExecutor;

import java.util.Hashtable;

import static stuff.logic.Array.*;
import static stuff.logic.AFunc.TwoType;

import static mindustry.logic.LExecutor.*;

public class TheInstruction{
    public Hashtable<String, Array> storage = new Hashtable<>();

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

        public AFunction(AFunc OpA, TwoType TT, int a, int b, int c, int d, int e, int result){
            this.OpA = OpA;
            this.TT = TT;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.result = result;
        }

        AFunction(){}

        @Override
        public void run(LExecutor exec){
            Array arr1 = null,
                  arr2 = null;
            
            if(result == varCounter || result == varUnit || result == varThis) return;
            
            Var A = exec.var(a),
                B = exec.var(b),
                R = exec.var(result);
            
            if(A.objval instanceof Array ar) {
                arr1 = ar;
                A.isobj = false;
                A.constant = false;
            }
            if(B.objval instanceof Array ar) {
                arr2 = ar;
                B.isobj = false;
                B.constant = false;
            }

            double s0 = exec.num(b),
                  s_1 = exec.num(c),
                    s = exec.num(e);
            boolean b1 = exec.bool(e);
            int s2 = exec.numi(b);
            int[] s3 = {s2, exec.numi(c), exec.numi(d)};
            
            try{
                switch(OpA){
                    case New -> {
                        R.objval = new Array(exec.numi(a), s2, exec.numi(c));
                        break;
                    }
                    case Add -> {                     
                        arr1.add(arr2);  
                        R.objval = arr1;
                    }
                    case Subtract -> {      
                        arr1.minus(arr2);                  
                        R.objval = arr1;
                    }
                    case Muliply -> {
                        switch(TT){
                            case array -> { 
                                arr1.prodEach(arr2);                              
                                R.objval = arr1;
                            }
                            case number -> {  
                                arr1.prod(s0);                              
                                R.objval = arr1;
                            }
                        }
                        break;
                    }
                    case Divide -> {
                        switch(TT){
                            case array -> {
                                arr1.divEach(arr2);
                                R.objval = arr1;
                            }
                            case number -> {
                                arr1.div(s0);
                                R.objval = arr1;
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
                                A.objval = arr1;
                            }
                            case number -> {
                                arr1.Change(s2, s_1);
                                A.objval = arr1;
                            }
                        }
                        break;
                    }
                    case CrossProduct -> {
                        R.objval = arr1.crossProd(arr2);
                        break;
                    }
                    case DotProd -> {
                        exec.setnum(result, arr1.dotProd(arr2));
                        break;
                    }
                    case Get -> {
                        switch(TT){
                            case array -> exec.setnum(result, arr1.getNum(s3));
                            case number -> exec.setnum(result, arr1.s[s2]);
                        }
                        break;
                    }
                    case ProductAll -> {
                        exec.setnum(result, productAll(arr1.s));
                        break;
                    }
                    case Resize -> {
                        arr1.Resize(s3, b1);
                        R.objval = arr1;
                        break;
                    }
                    case Shuffle -> {
                        arr1.shuffle();
                        A.objval = arr1;
                        break;
                    }
                    case Length -> {
                        switch(TT){
                            case number -> {exec.setnum(result, arr1.All); break;}
                            case array -> {R.objval = arr1.Length(); break;}
                        }
                    }
                    case Assign -> {
                        R.objval = arr1;
                    }
                }

                R.isobj = false;
                R.constant = false;
            }catch(Exception n){
                if(OpA.number) exec.setnum(result, 0d);
            }
        }
    }
}
