package stuff.util;

import java.util.ArrayDeque;
import java.util.Arrays;

import mindustry.logic.LExecutor;
import stuff.logic.Functions;

public abstract class Function{
    public Function f1, f2;
    public String inputName;
    public LExecutor exec;

    public void process(String[] args, Function f){
        ArrayDeque<String> nArgs = new ArrayDeque<>(Arrays.asList(args));
        for(int i=0; i<4; i++) nArgs.removeFirst();

        process(nArgs, f, 1);
    }

    static ArrayDeque<String> process(ArrayDeque<String> args, Function f, int i){
        String rev = args.pollFirst();
        Functions F_Enum = Functions.valueOf(rev);
        try{
            if(rev.equals("variable") || i > 2) throw new Exception();
            F_Enum =  Functions.valueOf(rev);
            f.f1 = F_Enum.nf.get(); 
            args = process(args, f.f1, ++i);
        }catch(Exception e){
            while(true){
                try{
                    Functions.valueOf(args.getFirst());
                    rev = args.pollFirst();
                }catch(Exception e2){ break; }
            }
            
            if(rev == null) f.f2 = new DVar("invalid");
            else f.f2 = new DVar(rev);
        }

        if(!F_Enum.isUnary){
            rev = args.pollFirst();
            try{
                if(rev.equals("variable") || i > 2) throw new Exception();
                F_Enum =  Functions.valueOf(rev);
                f.f2 = F_Enum.nf.get();
                args = process(args, f.f2, i);
            }catch(Exception e){
                while(true){
                    try{
                        Functions.valueOf(args.getFirst());
                        rev = args.pollFirst();
                    }catch(Exception e2){ break; }
                }

                if(rev == null) f.f2 = new DVar("invalid");
                else f.f2 = new DVar(rev);
            }
        }
        return args;
    }

    public abstract double evaluate(double x);

    public abstract Functions get();

    public static class DVar extends Function{
        public String name = "a";
        public int id;

        public DVar(String name){
            this.name = name;
        }

        @Override
        public double evaluate(double x) {
            return name.equals(inputName) ? x : exec.num(id);
        }

        @Override
        public String toString() {
            return name.concat(" ");
        }

        @Override
        public Functions get() {
            return Functions.variable;
        }
    }

    public static class Add extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) + f2.evaluate(x);
        }

        @Override
        public String toString() {
            return new StringBuilder("add ").append(f1).append(f2).toString();
        }

        @Override
        public Functions get() {
            return Functions.add;
        }
    }

    public static class Sub extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) - f2.evaluate(x);
        }

        @Override
        public String toString() {
            return new StringBuilder("sub ").append(f1).append(f2).toString();
        }

        @Override
        public Functions get() {
            return Functions.sub;
        }
    }
}
