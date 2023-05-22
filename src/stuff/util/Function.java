package stuff.util;

import java.util.ArrayList;

import stuff.logic.Functions;

public abstract class Function{
    //public static final Function[] New = {new Add(), new Sub()};
    //public static final String[] separateList = {",", "|", ":", "#", "?"};

    public Function f1, f2;
    public String inputName;

    /*public static Function process(String arg, Function f){
        return process(splitSpace(arg), f, 0, 0);
    }

    static Function process(String[] arg, Function f, int i, int j){
        try{
            f.f1 = Functions.valueOf(arg[j + 1]).nf.get(); 
        }
        catch(Exception e){
            f.f1 = new DVar(arg[j + 1], 0);
        }
        process(arg, f.f1, ++i, ++j);
    }

    static String[] splitSpace(String arg){
        int index = 0, prevIndex = 0;
        ArrayList<String> out = new ArrayList<>();

        while((index = arg.indexOf(" ", prevIndex)) != -1){
            out.add(arg.substring(prevIndex, index));
            index = prevIndex + 1;
        }

        return out.toArray(new String[out.size()]);
    }*/

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
            return name;
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
            return new StringBuilder("add").append(f1).append(f2).toString();
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
            return new StringBuilder("sub").append(f1).append(f2).toString();
        }

        @Override
        public Functions get() {
            return Functions.sub;
        }
    }
}
