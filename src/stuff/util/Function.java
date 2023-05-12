package stuff.util;

import mindustry.logic.LogicOp;

public abstract class Function{
    public Function f1, f2;
    public DVar var1, var2;

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

    public interface Evaluate{
        double evaluate(Function F);
    }

    

    public abstract boolean isUnary();
}
