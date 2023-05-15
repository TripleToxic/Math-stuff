package stuff.util;

import mindustry.logic.LExecutor;

public abstract class Function{
    public static final Function[] New = {new Add(), new Sub()};

    public Function f1, f2;
    public String inputName;

    public static Function process(LExecutor exec, String in){
        StringBuilder out = new StringBuilder();
        String buffer = "";

        buffer = in.substring(0, in.indexOf("("));

        return New[0];
    }

    public abstract double evaluate(double x);

    public static class DVar extends Function{
        public String name;
        public double value = 0;
    
        public DVar(String name){
            this.name = name;
        }

        @Override
        public double evaluate(double x) {
            return name.equals(inputName) ? x : value;
        }
    }

    public static class Add extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) + f2.evaluate(x);
        }
    }

    public static class Sub extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) - f2.evaluate(x);
        }
    }
}
