package stuff.util;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LAssembler.BVar;
import stuff.logic.FunctionEnum;

import static stuff.util.AdditionalFunction.*;

public class Function{
    public String functionName, inputname, a, b;
    int id1, id2, id3;
    FunctionEnum op;
    boolean recur;

    static int length = 5;

    public Function(String output, String input, FunctionEnum ope, String name1, String name2, boolean recurs, String recur_string, LAssembler builder){
        functionName = output;
        inputname = input;
        op = ope;
        a = name1;
        b = name2;
        recur = recurs;
        id1 = builder.var(name1);
        id2 = builder.var(name2);
        BVar v = builder.putVar("".concat(functionName).concat(" recur"));
        v.value = parseDouble(recur_string);
        id3 = v.id;
    }

    public Function(){}

    public double evaluate(LExecutor exec, double val){
        return evaluate(exec, val, new Function[length + 1], 0);
    }

    public double evaluate(LExecutor exec, double val, Function[] names, int i){
        names[i] = this;

        Function f1 = exec.obj(id1) instanceof Function f ? f : null, 
                 f2 = exec.obj(id2) instanceof Function f ? f : null;

        int p1 = check(names, f1), 
            p2 = check(names, f2);

        double out = op.isUnary ?
        op.eval.eval(
            f1 != null && i < length ?
                p1 != names.length ? (names[p1].recur ? exec.num(names[p1].id3) : 0) : f1.evaluate(exec, val, names, i + 1)
                :
                a.equals(inputname) ? val : exec.num(id1))
        :
        op.evals.eval(
            f1 != null && i < length ?
                p1 != names.length ? (names[p1].recur ? exec.num(names[p1].id3) : 0) : f1.evaluate(exec, val, names, i + 1)
                :
                a.equals(inputname) ? val : exec.num(id1),

            f2 != null && i < length ?
                p2 != names.length ? (names[p2].recur ? exec.num(names[p2].id3) : 0) : f2.evaluate(exec, val, names, i + 1) 
                :
                b.equals(inputname) ? val : exec.num(id2)
        );

        if(recur) exec.setnum(id3, out);

        return out;
    }

    private static int check(Function[] fs, Function f){
        if(f == null) return fs.length;
        int i = 0;
        while(!fs[i].functionName.equals(f.functionName)){
            i++;
            if(i == fs.length || fs[i] == null) return fs.length;
        }
        return i;
    }
}
