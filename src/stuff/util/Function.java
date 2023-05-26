package stuff.util;

import java.util.ArrayDeque;
import java.util.Arrays;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import stuff.logic.Functions;

public abstract class Function{
    public Function f1, f2;
    public String inputName;
    public LExecutor exec;

    public static void process(String[] args, Function f){
        ArrayDeque<String> nArgs = new ArrayDeque<>(Arrays.asList(args));
        for(int i=0; i<4; i++) nArgs.removeFirst();

        process(nArgs, f, 1);
    }

    static void process(ArrayDeque<String> args, Function f, int i){
        String rev = args.pollFirst();
        Functions F_Enum;
        try{
            if(rev.equals("variable") || i > 2) throw new Exception();
            F_Enum =  Functions.valueOf(rev);
            f.f1 = F_Enum.nf.get(); 
            process(args, f.f1, ++i);
        }catch(Exception e){
            try{
                while(true){
                    Functions.valueOf(args.getFirst());
                    rev = args.pollFirst();
                }
            }catch(Exception e2){}
            
            if(rev == null) f.f1 = new DVar("invalid");
            else f.f1 = new DVar(rev);
            F_Enum = Functions.variable;
        }

        if(!F_Enum.isUnary){
            rev = args.pollFirst();
            try{
                if(rev.equals("variable") || i > 2) throw new Exception();
                F_Enum =  Functions.valueOf(rev);
                f.f2 = F_Enum.nf.get();
                process(args, f.f2, i);
            }catch(Exception e){
                try{
                    while(true){
                        Functions.valueOf(args.getFirst());
                        rev = args.pollFirst();
                    }
                }catch(Exception e2){}

                if(rev == null) f.f2 = new DVar("invalid");
                else f.f2 = new DVar(rev);
            }
        }
    }

    public static void assign(Function f, LAssembler builder){
        if(f instanceof DVar var) {
            if(!var.name.equals(var.inputName))
                ((DVar)f).id = builder.var(var.name);
        }else{
            assign(f.f1, builder);
            if(!f.get().isUnary) assign(f.f2, builder);
        }
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
