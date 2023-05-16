package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public abstract class Function{
    public static final Function[] New = {new Add(), new Sub()};

    public Function f1, f2;
    public String inputName;

    public static void process(Function f, DVar var, LExecutor exec, LAssembler builder, String in){
        String buffer = new StringBuilder(in).reverse().toString(), buffer2 = "";
        int pos = 0;
        try{
            buffer2 = in.substring(in.indexOf("("), buffer.indexOf(","));
            try{
                f.f1 = new DVar("x x", (double)builder.getVar(buffer2).value);
            }catch(Exception e){
                
            }
            if(pos == 0) f.f1 = new DVar(in, pos);
        }catch(Exception e){f.f1 = new DVar("x x", 0d);}
        
        
    }

    public abstract double evaluate(double x);

    public abstract boolean isUnary();

    public static class DVar extends Function{
        public String name;
        public double value;
    
        public DVar(String name, double value){
            this.name = name;
            this.value = value;
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
