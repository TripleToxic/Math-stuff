package stuff.util;

public abstract class Function{
    public Function f1, f2;
    public DVar var1, var2;

    /**
        A double variable with name and value
    */
    public static class DVar{
        public String name;
        public double value;

        public DVar(String name){
            this.name = name;
        }
    }

    public abstract boolean isUnary();

}
