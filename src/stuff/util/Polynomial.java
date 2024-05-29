package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public class Polynomial extends Function{
    final int[] coefficents;

    public Polynomial(String[] strs, int degree, LAssembler builder){
        coefficents = new int[degree + 1];
        for(int i=0; i<degree + 1; i++){
            coefficents[i] = builder.var(strs[i]);
        }
    }

    @Override
    public double evaluate(LExecutor exec, double val) {
        double out = exec.num(coefficents[coefficents.length - 1]);
        for(int i=coefficents.length - 2; i>=0; i--){
            out = exec.num(coefficents[i]) + val*out;
        }
        return out;
    }

    @Override
    public double integral(LExecutor exec, double a, double b) {
        int L = coefficents.length;

        c1 = 0; 
        c2 = 0;

        if(a != 0d){
            c1 = exec.num(coefficents[L - 1])/(double)L;
            for(int i=L-2; i>=0; i--){
                c1 = exec.num(coefficents[i])/(double)(i+1) + a*c1;
            }
            c1 *= a;
        }
        
        if(b != 0d){
            c2 = exec.num(coefficents[L - 1])/(double)L;
            for(int i=L-2; i>=0; i--){
                c2 = exec.num(coefficents[i])/(double)(i+1) + b * c2;
            }
            c2 *= b;
        }
        return c2 - c1;
    }

    @Override
    protected Double compute() {
        return null;
    }
}
