package stuff.util;

import java.util.concurrent.*;

import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import stuff.logic.FunctionEnum;

public class NormalFunction extends Function{
    public final String functionName;
    final int id1, id2;
    final FunctionEnum op;
    boolean unchecked = true;
    NormalFunction f1, f2, parentFunction;
    private static final ForkJoinPool p = ForkJoinPool.commonPool();

    static final int length = 5;

    //values for fork-join
    LExecutor exec;
    double val;
    int i;

    NormalFunction init(){
        exec = parentFunction.exec;
        val = parentFunction.val;
        i = parentFunction.i + 1;

        return this;
    }

    public NormalFunction(String output, FunctionEnum ope, String name1, String name2, LAssembler builder){
        functionName = output;
        op = ope;

        if(!name1.equals("x")) id1 = builder.var(name1);
        else id1 = 0;

        if(!name2.equals("x")) id2 = builder.var(name2);
        else id2 = 0;
    }

    @Override
    public double evaluate(LExecutor exec, double val){
        this.exec = exec;
        this.val = val;
        return p.invoke(this);
    }

    @Override
    protected Double compute(){
        return evaluate();
    }

    public double evaluate(){
        if(unchecked){
            f1 = exec.obj(id1) instanceof NormalFunction f ? (f.search() ? null : f.setParent(this)) : null;
            f2 = exec.obj(id2) instanceof NormalFunction f ? (f.search() ? null : f.setParent(this)) : null;
            
            unchecked = false;
        }

        if(op.isUnary){
            return op.eval.eval(
                f1 != null && i < length - 1 ?
                    f1.init().evaluate()
                    :
                    id1 == 0 ? val : exec.num(id1)
            );
        }

        double left = 0;
        final boolean b = f1 != null && i < length - 1;

        if(b) f1.init().fork();
        else left = id1 == 0 ? val : exec.num(id1);
        
        double right = f2 != null && i < length - 1 ?
            f2.init().evaluate()
            :
            id2 == 0 ? val : exec.num(id2);

        return op.evals.eval(b ? f1.join() : left, right);
    }

    NormalFunction setParent(NormalFunction parent){
        parentFunction = parent;
        return this;
    }

    boolean search(){
        NormalFunction check = parentFunction;
        while(true){
            if(check == null) return false;
            if(check == this) return true;
            check = check.parentFunction;
        }
    }

    @Override
    public double integral(LExecutor exec, double a, double b){
        if(a == b) return 0;

        c1 = 0.5d * (b - a);
        c2 = 0.5d * (b + a);

        double f2 = evaluate(exec, c1 * x2 + c2),
               f3 = evaluate(exec, c1 * x3 + c2),
               f4 = evaluate(exec, c1 * x4 + c2),
               f5 = evaluate(exec, c1 * x5 + c2),
               f6 = evaluate(exec, c1 * x6 + c2),
               f7 = evaluate(exec, c1 * x7 + c2),
                
               f1 = w1 * evaluate(exec, c2),
               f23 = w23 * (f2 + f3),
               f45 = w45 * (f4 + f5),
               f67 = w67 * (f6 + f7),

               total = c1 * (f1 + f23 + f45 + f67);
        return total;
    }
}
