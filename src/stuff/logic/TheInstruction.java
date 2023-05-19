package stuff.logic;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import stuff.util.Array;
import stuff.util.Complex;

import static stuff.util.Array.*;
import static stuff.logic.AFunc.TwoType;
import static mindustry.logic.LExecutor.*;

public class TheInstruction{
    public static class ComplexOperationI implements LInstruction{
        public CFunc Op = CFunc.New;
        public int r, i, result;

        public ComplexOperationI(CFunc Op, int r, int i, int result){
            this.Op = Op;
            this.r = r;
            this.i = i;
            this.result = result;
        }

        ComplexOperationI(){}

        @Override
        public void run(LExecutor exec){
            if(Op == CFunc.New) {exec.setobj(result, new Complex(exec.num(r), exec.num(i)).toString()); return;}
            

            if(Op == CFunc.get){
                if(exec.obj(result) instanceof String pRe){
                    Complex Re = new Complex(pRe);
                    exec.setnum(r, Re.r);
                    exec.setnum(i, Re.i);
                }else{
                    exec.setnum(r, 0d);
                    exec.setnum(i, 0d);
                }
                return;
            }

            if(Op.real) {exec.setnum(result, Op.RealFunction.get(exec.num(r))); return;}
            
            if(!(exec.obj(r) instanceof String pR)){exec.setobj(result, null); return;}

            Complex R = new Complex(pR);
            if(Op.unary){exec.setobj(result, Op.Unary.get(R).toString()); return;}

            if(!(exec.obj(i) instanceof String pI)){exec.setobj(result, null); return;}

            Complex I = new Complex(pI);
            exec.setobj(result, Op.Binary.get(R, I).toString());
        }
    }

    //Currently halted
    /* 
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
            Arrays Arrs = Arrays.stuff.get(exec.build.toString());
            if(Arrs == null) Arrs = new Arrays();
            
            Array arr1 = Arrs.storage.get(A),
                  arr2 = Arrs.storage.get(B);

            double s0 = exec.num(b),
                  s_1 = exec.num(c),
                    s = exec.num(e);
            boolean b1 = exec.bool(e);
            int s2 = exec.numi(b);
            int[] s3 = {s2, exec.numi(c), exec.numi(d)};
            
            try{
                switch(OpA){
                    case New -> {
                        Arrs.storage.put(Result, new Array(exec.numi(a), s2, exec.numi(c)));
                        break;
                    }
                    case Add -> {                     
                        arr1.add(arr2);  
                        Arrs.storage.put(Result, arr1);
                    }
                    case Subtract -> {      
                        arr1.minus(arr2);                  
                        Arrs.storage.put(Result, arr1);
                    }
                    case Muliply -> {
                        switch(TT){
                            case array -> { 
                                arr1.prodEach(arr2);                              
                                Arrs.storage.put(Result, arr1);
                            }
                            case number -> {  
                                arr1.prod(s0);                              
                                Arrs.storage.put(Result, arr1);
                            }
                        }
                        break;
                    }
                    case Divide -> {
                        switch(TT){
                            case array -> {
                                arr1.divEach(arr2);
                                Arrs.storage.put(Result, arr1);
                            }
                            case number -> {
                                arr1.div(s0);
                                Arrs.storage.put(Result, arr1);
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
                                Arrs.storage.put(A, arr1);
                            }
                            case number -> {
                                arr1.Change(s2, s_1);
                                Arrs.storage.put(A, arr1);
                            }
                        }
                        break;
                    }
                    case CrossProduct -> {
                        Arrs.storage.put(Result, arr1.crossProd(arr2));
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
                        Arrs.storage.put(Result, arr1);
                        break;
                    }
                    case Shuffle -> {
                        arr1.shuffle();
                        Arrs.storage.put(A, arr1);
                        break;
                    }
                    case Length -> {
                        switch(TT){
                            case number -> {exec.setnum(result, arr1.All); break;}
                            case array -> {Arrs.storage.put(Result, arr1.Length()); break;}
                        }
                    }
                    case Assign -> {
                        Arrs.storage.put(Result, arr1);
                    }
                }

                Arrays.stuff.put(exec.build.toString(), Arrs);
            }catch(Exception n){
                if(OpA.number) exec.setnum(result, 0d);
            }
        }
    }*/
}
