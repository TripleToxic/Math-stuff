package stuff.util;

import java.util.Arrays;

import stuff.logic.Functions;

public abstract class Function{
    public Function f1, f2;
    public String inputName;

    public static Function process(String[] args, Function f){
        return process(Arrays.copyOfRange(args, 3, args.length), f, 1);
    }

    static Function process(String[] args, Function f, int i){
        Functions F_Enum;
        try{
            if(args[i].equals("variable") || i >= 7) throw new Exception();
            F_Enum =  Functions.valueOf(args[i]);
            f.f1 = F_Enum.nf.get();
            process(args, f.f1, ++i);
        }catch(Exception e){
            f.f1 = new DVar(args[i], 0);
            F_Enum = Functions.variable;
        }

        if(F_Enum.isUnary){;
            try{
                if(args[i].equals("variable") || i >= 7) throw new Exception();
                F_Enum =  Functions.valueOf(args[i + 1]);
                f.f2 = F_Enum.nf.get();
                process(args, f.f1, ++i);
            }catch(Exception e){
                f.f2 = new DVar(args[i + 1], 0);
                F_Enum = Functions.variable;
            }
        }
        return f;
    }

    public abstract double evaluate(double x);

    public abstract Functions get();

    public static class DVar extends Function{
        public String name = "default";
        public double value = 0;
    
        public DVar(String name, double value){
            this.name = name;
            this.value = value;
        }

        public DVar(){}

        @Override
        public double evaluate(double x) {
            return name.equals(inputName) ? x : value;
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
