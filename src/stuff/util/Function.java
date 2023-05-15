package stuff.util;

import mindustry.logic.LExecutor;

public abstract class Function{
    public static final Function[] New = {new Add(), new Sub()};

    public Function f1, f2;
    public String inputName;

    public static void process(Function f, LExecutor exec, String in, int subIn){
        StringBuilder out = new StringBuilder();
        String buffer = "";

        f.f1 = New[Integer.parseInt(in.substring(subIn + 1, in.indexOf("(")))];
    }

    public abstract double evaluate(double x);

    public abstract boolean isUnary();

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

		@Override
		public boolean isUnary() {
			return true;
		}
    }

    public static class Add extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) + f2.evaluate(x);
        }

        @Override
		public boolean isUnary() {
			return false;
		}
    }

    public static class Sub extends Function{
        @Override
        public double evaluate(double x){
           return f1.evaluate(x) - f2.evaluate(x);
        }

        @Override
		public boolean isUnary() {
			return false;
		}
    }
}
