package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public class Polynomial implements FunctionEval{
    String functionName;
    int[] coefficents;

    public Polynomial(String[] strs, int degree, LAssembler builder){
        coefficents = new int[degree + 1];
        for(int i=0; i<degree + 1; i++){
            coefficents[i] = builder.var(strs[i]);
        }
    }

    @Override
    public double evaluate(LExecutor exec, double val) {
        double out = exec.num(coefficents[coefficents.length]);
        for(int i=coefficents.length - 2; i>=0; i--){
            out = exec.num(coefficents[i]) + val*out;
        }
        return out;
    }
}
