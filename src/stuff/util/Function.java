package stuff.util;

public class Function{
    public Function f1, f2;
    public DVar var1, var2;
    public boolean isUnary;

    /**
        A double variable with name and value
    */
    public static class DVar{
        public String name;
        public double value = 0;

        public DVar(String name){
            this.name = name;
        }
    }

    public static class Add extends Function implements BinaryFunctionStuff{

        @Override
        public double evaluate(Function F){
            return 0;
        }
    }

    public interface BinaryFunctionStuff{
        double evaluate(Function F);
    }
}
