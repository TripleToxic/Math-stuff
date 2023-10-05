package stuff.logic;

import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.LInstruction;
import mindustry.logic.LExecutor.Var;
//import stuff.util.Array;
import stuff.util.Complex;
import stuff.util.Function;

//import static stuff.util.Array.*;
//import static stuff.logic.AFunc.TwoType;

public class TheInstruction{
    public static void setcomplex(LExecutor exec, int index, Complex c){
        exec.setnum(index + 1, c.r);
        exec.setnum(index + 2, c.i);
    }

    public static boolean invalid(double a){
        return Double.isNaN(a) || Double.isInfinite(a);
    }

    public static Complex complex(LExecutor exec, int index){
        Var v = exec.var(index);
        Complex c = v.isobj && v.objval instanceof Complex complex ? complex : null;
        if(c.unchecked && c != null){
            c.r = exec.num(index + 1);
            c.i = exec.num(index + 2);
            c.unchecked = false;
        }
        return c;
    }

    public static class ComplexOperationI implements LInstruction{
        public CFunc Op = CFunc.set;
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
            Complex c1 = complex(exec, result);
            if(c1 == null) return;

            if(Op == CFunc.set){
                setcomplex(exec, result, c1.set(exec.num(r), exec.num(i)));
                return;
            }
            
            if(Op == CFunc.get){
                exec.setnum(r, c1.r);
                exec.setnum(i, c1.i);
                return;
            }
            
            Complex c2 = complex(exec, r);
            if(c2 == null){
                setcomplex(exec, result, c1.Default());
                return;
            }

            Complex c2s = r == result ? c2 : new Complex(c2);
            if(Op.unary){
                c1.set(Op.Unary.get(c2));
            }else{
                Complex c3 = complex(exec, i);
                if(c3 == null){
                    setcomplex(exec, result, c1.Default());
                    return;
                }

                c1.set(Op.Binary.get(c2, c3));
            }
            setcomplex(exec, result, c1);
            c2 = c2s;
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
    public static class FunctionOperationI implements LInstruction{
        public int F, x, result;

        public FunctionOperationI(int F, int x, int result){
            this.F = F;
            this.x = x;
            this.result = result;
        }

        @Override
        public void run(LExecutor exec) {
            exec.setnum(result, exec.obj(F) instanceof Function f ?
                f.evaluate(exec, exec.num(x)) : 0
            );
        }
    }

    //Constants for 7-points Gaussian quadrature. x1 = 0
    static double
    x2 = 0.405845151377397167d,
    x3 = -0.405845151377397167d,
    x4 = 0.74153118559939444d,
    x5 = -0.74153118559939444d,
    x6 = 0.949107912342758525d,
    x7 = -0.949107912342758525,

    w1 = 0.417959183673469388d,
    w23 = 0.38183005050511894d,
    w45 = 0.279705391489276668d,
    w67 = 0.129484966168869693d;

    public static class IntegralI implements LInstruction{
        public int result, F, a, b;

        public IntegralI(int result, int F, int a, int b){
            this.result = result;
            this.F = F;
            this.a = a;
            this.b = b;
        }

        IntegralI(){}

        @Override
        public void run(LExecutor exec) {
            double aI = exec.num(a),
                   bI = exec.num(b);

            double avg = 0.5d * (bI + aI);                
            if(exec.obj(F) instanceof Function f && aI != bI && Math.abs(bI - avg) <= 5d)
                exec.setnum(result, f.integral(exec, aI, bI));
            else 
                exec.setnum(result, 0d);
        }
        
    }

    public static class RootFindingI implements LInstruction{
        public int result, F, maxIter, guess_0, guess_1;
        public RootFindingEnum op;

        public RootFindingI(RootFindingEnum op, int result, int F, int maxIter, int guess_0, int guess_1){
            this.op = op;
            this.result = result;
            this.F = F;
            this.maxIter = maxIter;
            this.guess_0 = guess_0;
            this.guess_1 = guess_1;
        }

        RootFindingI(){}

        @Override
        public void run(LExecutor exec){
            
            int max = exec.numi(maxIter);

            double x_0 = exec.num(guess_0),
                x_1 = exec.num(guess_1),
                f0, f1, root, buffer;

            if(exec.obj(F) instanceof Function f && max > 0){
                switch(op){
                    
                    case Bisection -> {
                        f0 = f.evaluate(exec, x_0);
                        f1 = f.evaluate(exec, x_1);
                        //Check if each of two number are positive and negative for bisection process
                        if((Double.doubleToRawLongBits(f0) ^ Double.doubleToRawLongBits(f1)) < 0)
                            for(int i=0; i<max; i++, f0 = f.evaluate(exec, x_0), f1 = f.evaluate(exec, x_1)){
                                
                            }
                        else exec.setobj(result, null);
                    }

                    case Secant -> {

                        for(int i=0; i<max; i++){
                            f0 = f.evaluate(exec, x_0);
                            f1 = f.evaluate(exec, x_1);
                            buffer = x_1 - x_0; 
                            root = f0 * buffer/(f1 - f0);
                            if(invalid(root)){
                                exec.setnum(result, exec.num(guess_0));
                                return;
                            }
                            if(Math.abs(root) > Math.abs(buffer + root))
                                x_0 -= root;
                            else
                                x_1 = x_0 - root;
                        }
                    }
                }
            }
            else exec.setobj(result, null);
        }
    }
}
