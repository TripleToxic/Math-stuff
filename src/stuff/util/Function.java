package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LAssembler.BVar;
import stuff.logic.FunctionEnum;
import java.util.ArrayList;

import static stuff.util.AdditionalFunction.*;

public class Function implements FunctionEval{
    public String functionName;
    int id1, id2, id3, ptr1, ptr2;
    FunctionEnum op;
    boolean recur, unchecked = true;
    Function f1, f2;
    double recurBuffer;

    static final int length = 5;

    public Function(String output, FunctionEnum ope, String name1, String name2, boolean recurs, String recur_string, LAssembler builder){
        functionName = output;
        op = ope;
        recur = recurs;

        if(!name1.equals("x")) id1 = builder.var(name1);
        if(!name2.equals("x")) id2 = builder.var(name2);

        if(recur){
            BVar v = builder.putVar("recur ".concat(functionName));
            v.value = parseDouble(recur_string);
            id3 = v.id;
        }
    }

    public Function(){}

    @Override
    public double evaluate(LExecutor exec, double val){
        ArrayList<Function> names = new ArrayList<>();
        double out = evaluate(exec, val, names, 0);
        for(int i=0; i<names.size(); i++){
            if(names.get(i).recur) exec.setnum(names.get(i).id3, recurBuffer);
        }
        return out;
    }

    public double evaluate(LExecutor exec, double val, ArrayList<Function> names, int i){
        names.add(this);

        if(unchecked){
            f1 = exec.obj(id1) instanceof Function f ? f : null;
            f2 = exec.obj(id2) instanceof Function f ? f : null;
            
            ptr1 = check(names, f1);
            ptr2 = check(names, f2);
            unchecked = false;
        }

        double out = op.isUnary ?
        op.eval.eval(
            f1 != null && i < length - 1 ?
                ptr1 != -1 ? (names.get(ptr1).recur ? exec.num(names.get(ptr1).id3) : 0) : f1.evaluate(exec, val, names, i + 1)
                :
                id1 == 0 ? val : exec.num(id1))
        :
        op.evals.eval(
            f1 != null && i < length - 1 ?
                ptr1 != -1 ? (names.get(ptr1).recur ? exec.num(names.get(ptr1).id3) : 0) : f1.evaluate(exec, val, names, i + 1)
                :
                id1 == 0 ? val : exec.num(id1),

            f2 != null && i < length - 1 ?
                ptr2 != -1 ? (names.get(ptr2).recur ? exec.num(names.get(ptr2).id3) : 0) : f2.evaluate(exec, val, names, i + 1) 
                :
                id2 == 0 ? val : exec.num(id2)
        );

        if(recur) recurBuffer = out;

        return out;
    }

    private static int check(ArrayList<Function> fs, Function f){
        if(f == null) return -1;
        int i = 0;
        while(!fs.get(i).functionName.equals(f.functionName)){
            i++;
            if(i == fs.size() || fs.get(i) == null) return -1;
        }
        return i;
    }
}
