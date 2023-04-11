package stuff.logic;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.*;

import static stuff.logic.Array.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

import static stuff.logic.AFunc.TwoType;

public class TheInstruction{
    public Hashtable<String, Array> storage = new Hashtable<>();

    public static HashMap<Integer, TheInstruction> BigStorage = new HashMap<>();

    private static LinkedList<Integer> memory = new LinkedList<>();
    
    public static final int cleanup = 200000;
    private static int counter = 0;
    private static Random R = new Random();

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
        public int a, b, c, d, e, result, h;
        public String A, B, Result;

        public AFunction(AFunc OpA, TwoType TT, int a, int b, int c, int d, int e, int result, String A, String B, String Result, int h){
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
            this.h = h;
        }

        AFunction(){}

        @Override
        public void run(LExecutor exec){
            if(counter >= cleanup){
                for(Integer l : memory){
                    if(BigStorage.containsKey(l)) BigStorage.remove(l);
                }
                memory.addAll(BigStorage.keySet());
                counter = 0;
            }counter++;

            int i = exec.numi(h);

            TheInstruction TInst = BigStorage.get(i);
            if(TInst == null) {
                TInst = new TheInstruction();
            }
            
            Array arr1 = TInst.storage.get(A),
                  arr2 = TInst.storage.get(B);

            if(arr1 == null) arr1 = new Array(0, 0, 1);
            if(arr2 == null) arr2 = new Array(0, 0, 1);
            
            double s0 = exec.num(b),
                  s_1 = exec.num(c),
                    s = exec.num(e);
            boolean b1 = exec.bool(e);
            int s2 = exec.numi(b);
            int[] s3 = {s2, exec.numi(c), exec.numi(d)};
            
            try{
                switch(OpA){
                    case New -> {
                        TInst.storage.put(Result, new Array(exec.numi(a), s2, exec.numi(c)));
                        break;
                    }
                    case Add -> {                     
                        arr1.add(arr2);  
                        TInst.storage.put(Result, arr1);
                    }
                    case Subtract -> {      
                        arr1.minus(arr2);                  
                        TInst.storage.put(Result, arr1);
                    }
                    case Muliply -> {
                        switch(TT){
                            case array -> { 
                                arr1.prodEach(arr2);                              
                                TInst.storage.put(Result, arr1);
                            }
                            case number -> {  
                                arr1.prod(s0);                              
                                TInst.storage.put(Result, arr1);
                            }
                        }
                        break;
                    }
                    case Divide -> {
                        switch(TT){
                            case array -> {
                                arr1.divEach(arr2);
                                TInst.storage.put(Result, arr1);
                            }
                            case number -> {
                                arr1.div(s0);
                                TInst.storage.put(Result, arr1);
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
                                TInst.storage.put(A, arr1);
                            }
                            case number -> {
                                arr1.Change(s2, s_1);
                                TInst.storage.put(A, arr1);
                            }
                        }
                        break;
                    }
                    case CrossProduct -> {
                        TInst.storage.put(Result, arr1.crossProd(arr2));
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
                        TInst.storage.put(Result, arr1);
                        break;
                    }
                    case Shuffle -> {
                        arr1.shuffle();
                        TInst.storage.put(A, arr1);
                        break;
                    }
                    case Length -> {
                        switch(TT){
                            case number -> {exec.setnum(result, arr1.All); break;}
                            case array -> {TInst.storage.put(Result, arr1.Length()); break;}
                        }
                    }
                    case Assign -> {
                        TInst.storage.put(Result, arr1);
                    }
                }

                BigStorage.remove(i);

                i = R.nextInt();
                while(BigStorage.containsKey(i) || memory.contains(i) || i == 0){
                    i = R.nextInt();
                }
                exec.setnum(h, i);

                BigStorage.put(i, TInst);
            }catch(Exception n){
                if(OpA.number) exec.setnum(result, 0d);
            }
        }
    }
}
