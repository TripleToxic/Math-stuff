package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;

public class Polynomial implements Function{
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
        double out = exec.num(coefficents[coefficents.length - 1]);
        for(int i=coefficents.length - 2; i>=0; i--){
            out = exec.num(coefficents[i]) + val*out;
        }
        return out;
    }

    @Override
    public double integral(LExecutor exec, double a, double b) {
        int L = coefficents.length;
        double total_a = 0, total_b = 0;
        if(a != 0d){
            total_a = exec.num(coefficents[L - 1])/(double)L;
            for(int i=L-2; i>=0; i--){
                total_a = exec.num(coefficents[i])/(double)(i+1) + a*total_a;
            }
            total_a *= a;
        }
        if(b != 0d){
            total_b = exec.num(coefficents[L - 1])/(double)L;
            for(int i=L-2; i>=0; i--){
                total_b = exec.num(coefficents[i])/(double)(i+1) + b*total_b;
            }
            total_b *= b;
        }
        return total_b - total_a;
    }
}
