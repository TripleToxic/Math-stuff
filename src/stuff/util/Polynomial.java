package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public class Polynomial implements FunctionEval{
    String functionName;
    int[] coefficents;

    public Polynomial(String[] strs, int degree, LAssembler builder){
        coefficents = new int[strs.length];
        for(int i=0; i<degree + 1; i++){
            coefficents[i] = builder.var(strs[i]);
        }
    }

    @Override
    public double evaluate(LExecutor exec, double val) {
        double out = exec.num(coefficents[0]), xn = val;
        for(int i=1; i<coefficents.length; i++){
            out += exec.num(coefficents[i]) * xn;
            xn *= val;
        }
        return out;
    }
}
