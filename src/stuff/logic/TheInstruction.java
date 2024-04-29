package stuff.logic;

import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.LInstruction;
import mindustry.logic.LExecutor.Var;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;
import stuff.util.Matrix;
import stuff.util.Complex;
import stuff.util.Function;
import stuff.util.NormalFunction;
import stuff.util.Polynomial;

import static stuff.logic.FunctionEnum.*;

public class TheInstruction{
    public static void setcomplex(LExecutor exec, int index, Complex c){
        exec.setnum(index + 1, c.r);
        exec.setnum(index + 2, c.i);
    }

    public static boolean invalid(double a){
        return Double.isNaN(a) || Double.isInfinite(a);
    }

    public static Complex complex(LExecutor exec, int index){
        Var v = exec.var(index);
        Complex c = v.isobj && v.objval instanceof Complex complex ? complex : null;
        if(c.unchecked && c != null){
            c.r = exec.num(index + 1);
            c.i = exec.num(index + 2);
            c.unchecked = false;
        }
        return c;
    }

    public static class ComplexOperationI implements LInstruction{
        public CFunc Op = CFunc.set;
        public int r, i, result;

        public ComplexOperationI(CFunc Op, int r, int i, int result){
            this.Op = Op;
            this.r = r;
            this.i = i;
            this.result = result;
        }

        ComplexOperationI(){}

        @Override
        public void run(LExecutor exec){
            Complex R = complex(exec, result);
            if(R == null) return;

            if(Op == CFunc.set){
                setcomplex(exec, result, R.set(exec.num(r), exec.num(i)));
                return;
            }
            
            if(Op == CFunc.get){
                exec.setnum(r, R.r);
                exec.setnum(i, R.i);
                return;
            }
            
            Complex c1 = complex(exec, r);
            if(c1 == null){
                setcomplex(exec, result, R.Default());
                return;
            }

            if(Op.unary){
                R.set(Op.Unary.get(c1, R));
            }else{
                Complex c2 = complex(exec, i);
                if(c2 == null){
                    setcomplex(exec, result, R.Default());
                    return;
                }

                R.set(Op.Binary.get(c1, c2, R));
            }
        }
    }

    public static class AllocateMatrixI implements LInstruction{
        public int mem, row, column, starter, result;

        public AllocateMatrixI(int mem, int row, int column, int starter, int result){
            this.mem = mem;
            this.row = row;
            this.column = column;
            this.starter = starter;
            this.result = result;
        }

        @Override
        public void run(LExecutor exec) {
            Object o = exec.obj(result),
                   memo = exec.obj(mem);
            if(memo instanceof MemoryBuild memory){
                if(o instanceof Matrix arr){
                    exec.setobj(result, arr.set(memory, exec.numi(row), exec.numi(column), exec.numi(starter)));
                }else{
                    exec.setobj(result, new Matrix(memory, exec.numi(row), exec.numi(column), exec.numi(starter)));
                }
            }
        }
    }
    
    public static class FunctionOperationI implements LInstruction{
        public int F, x, result;

        public FunctionOperationI(int F, int x, int result){
            this.F = F;
            this.x = x;
            this.result = result;
        }

        @Override
        public void run(LExecutor exec) {
            exec.setnum(result, exec.obj(F) instanceof Function f ?
                f.evaluate(exec, exec.num(x)) : 0
            );
        }
    }

    public static class IntegralI implements LInstruction{
        public int result, F, a, b;

        public IntegralI(int result, int F, int a, int b){
            this.result = result;
            this.F = F;
            this.a = a;
            this.b = b;
        }

        IntegralI(){}

        @Override
        public void run(LExecutor exec) {
            double aI = exec.num(a),
                   bI = exec.num(b);
          
            if(exec.obj(F) instanceof NormalFunction f && Math.abs(bI - aI) <= 10d)
                exec.setnum(result, bI == aI ? 0 : f.integral(exec, aI, bI));
            else if(exec.obj(F) instanceof Polynomial p)
                exec.setnum(result, bI == aI ? 0 : p.integral(exec, aI, bI));
            else
                exec.setobj(result, null);
        }
    }

    public static class RootFindingI implements LInstruction{
        public int result, F, maxIter, guess_0, guess_1;
        public RootFindingEnum op;

        double f0, f1, root, buffer, tol = 0.0000000000001d,
        previousx_0 = 0, previousx_1 = 0, previousResult = 0;
        boolean first = true;
        
        public RootFindingI(RootFindingEnum op, int result, int F, int maxIter, int guess_0, int guess_1){
            this.op = op;
            this.result = result;
            this.F = F;
            this.maxIter = maxIter;
            this.guess_0 = guess_0;
            this.guess_1 = guess_1;
        }
        
        RootFindingI(){}
        
        @Override
        public void run(LExecutor exec){
            double x_0 = exec.num(guess_0),
            x_1 = exec.num(guess_1);

            if(first){
                first = false;
            }else{
                if(previousx_0 == x_0 && previousx_1 == x_1){
                    exec.setnum(result, previousResult);
                    return;
                }

                previousx_0 = x_0;
                previousx_1 = x_1;
            }
            
            int max = exec.numi(maxIter);
            max = Math.min(max, 10); // True maximum
            

            if(exec.obj(F) instanceof Function f && max > 0 && x_0 != x_1){
                f0 = f.evaluate(exec, x_0);
                f1 = f.evaluate(exec, x_1);
                if(Math.abs(f0) < tol) exec.setnum(result, f0);

                else if(Math.abs(f1) < tol) exec.setnum(result, f1);
                switch(op){
                    case Bisection -> {
                        double mid = (x_0 + x_1) / 2d, fmid = 0;
                        
                        //Check if each of two number are positive and negative for bisection process
                        if(f0 * f1 <= 0){
                            for(int i=0; i<max; i++){
                                mid = (x_0 + x_1) / 2d;
                                fmid = f.evaluate(exec, mid);
                                
                                if(invalid(fmid)){
                                    exec.setobj(result, null);
                                    return;
                                }

                                if(Math.abs(fmid) < tol) break;
                                if(fmid * f1 > 0){
                                    x_0 = mid;
                                }
                                else{
                                    x_1 = mid;
                                }

                            }

                            if(Math.abs(fmid) > tol) exec.setobj(result, null);
                            else{
                                exec.setnum(result, mid);
                                previousResult = mid;
                            }
                        }
                        else exec.setobj(result, null);
                    }

                    case Regula_Falsi -> {
                        long l1 = l(f0),
                             l2 = l(f1);
                        if(((l1 ^ l2) < 0)){
                            for(int i=0; i<max; i++){
                                f0 = f.evaluate(exec, x_0);
                                f1 = f.evaluate(exec, x_1);
                                root = x_0 - f0 * (x_1 - x_0)/(f1 - f0);
                                
                                if(invalid(root)){
                                    exec.setobj(result, null);
                                    return;
                                }

                            }
                        

                            if(root > tol) exec.setobj(result, null);
                            else exec.setnum(result, root);
                        }
                        else exec.setobj(result, null);
                    }

                    case Secant -> {
                        for(int i=0; i<max; i++){
                            f1 = f.evaluate(exec, x_1);
                            if(f1 < tol) break;
                            f0 = f.evaluate(exec, x_0);
                            root = x_0 - f0 * (x_1 - x_0)/(f1 - f0);
                            if(invalid(root)){
                                exec.setobj(result, null);
                                return;
                            }
                            x_0 = x_1;
                            x_1 = root;
                        }

                        if(f1 > tol) exec.setobj(result, null);
                        else exec.setnum(result, x_1);
                    }

                    case ModifiedSecant -> {
                        double zero = 1;
                        for(int i=0; i<max; i++){
                            f0 = f.evaluate(exec, x_0);
                            f1 = f.evaluate(exec, x_1);
                            buffer = x_1 - x_0; 
                            root = f0 * buffer/(f1 - f0);
                            if(invalid(root)){
                                exec.setobj(result, null);
                                return;
                            }
                            if(Math.abs(root) > Math.abs(buffer + root)){
                                x_0 -= root;
                                root = x_0;
                                zero = f0;
                            }
                            else{
                                x_1 = x_0 - root;
                                root = x_1;
                                zero = f1;
                            }
                            if(zero < tol) break;
                        }

                        if(zero > tol) exec.setobj(result, null);
                        else exec.setnum(result, root);
                    }
                }
            }
            else exec.setobj(result, null);
        }
    }
}
