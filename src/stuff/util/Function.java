package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public abstract class Function{
    public static final Function[] New = {new Add(), new Sub()};
    public static final String[] separateList = {",", "|", ":", "#", "?"};

    public Function f1, f2;
    public String inputName;

    public static void process(Function f, LExecutor exec, LAssembler builder, String in){
        process(f, exec, builder, in, 0);
    }

    private static void process(Function f, LExecutor exec, LAssembler builder, String in, int count){
        String buffer2 = "";
        
        buffer2 = in.substring(in.indexOf("("), in.indexOf(separateList[count]));
        try{
            // Check for number
            f.f1 = new DVar(buffer2, Double.parseDouble(buffer2));
        }catch(Exception e){
            //Check for variable
            if(builder.vars.containsKey(buffer2)){
                f.f1 = new DVar(buffer2, exec.num(builder.vars.get(buffer2).id));
            }else{// Otherwise, assign a function to the object
                f.f1 = New[Integer.parseInt(buffer2.substring(1, 2))];
                process(f.f1, exec, builder, buffer2, ++count);
            }
        }
        
        if(!f.isUnary()){
            buffer2 = in.substring(in.indexOf(separateList[count] + 1));
            try{
                // Check for number
                f.f1 = new DVar(buffer2, Double.parseDouble(buffer2));
            }catch(Exception e){
            //Check for variable
                if(builder.vars.containsKey(buffer2)){
                    f.f1 = new DVar(buffer2, exec.num(builder.vars.get(buffer2).id));
                }else{// Otherwise, assign a function to the object
                    f.f1 = New[Integer.parseInt(buffer2.substring(1, 2))];
                    process(f.f1, exec, builder, buffer2, ++count);
                }
            }
        }
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

        @Override
        public String toString() {
            return name;
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

        @Override
        public String toString() {
            return new StringBuilder("add").append(f1).append(f2).toString();
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

        @Override
        public String toString() {
            return new StringBuilder("sub").append(f1).append(f2).toString();
        }
    }
}
