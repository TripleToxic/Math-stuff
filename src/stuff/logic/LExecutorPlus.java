package stuff.logic;

import mindustry.logic.LExecutor;

import static mindustry.Vars.*;

public class LExecutorPlus extends LExecutor {

    private static boolean invalid(double d){
        return Double.isNaN(d) || Double.isInfinite(d);
    }

    public Var[] varvect(int[] index){
        int count = index.length;
        Var[] arr = new Var[count];
        for(int i=0; i<count; i++){
            Var k = index[i] < 0 ? logicVars.get(-index[i]) : vars[index[i]];
            arr[i] = k;
        }
        return arr;
    }

    public double[] vect(int[] index){
        int count = index.length;
        double[] arr = new double[count];
        for(int i=0; i<count; i++){
            Var v = var(index[i]);
            double d = v.isobj ? v.objval != null ? 1 : 0 : invalid(v.numval) ? 0 : v.numval;
            arr[i] = d;
        }
        return arr;
    }

    public void setvect(int[] index, double[] vect){
        int count = index.length;
        for(int i=0; i<count; i++){
            for(int k=0; k<1; k++){
                Var v = var(index[i]);
                if(v.constant) break;
                if(invalid(vect[i])){
                    v.objval = null;
                    v.isobj = true;
                }else{
                    v.numval = vect[i];
                    v.objval = null;
                    v.isobj = false;
                } 
            }
        }
    }

    public interface LInstructionPlus{
        void run(LExecutorPlus exec);
    }
}
