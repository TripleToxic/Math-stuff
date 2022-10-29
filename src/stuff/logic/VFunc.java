package stuff.logic;

import static stuff.AdditionalFunction.*;

public enum VFunc {
    addV("+", (a, b) -> AddVector(a, b)),
    subV("-", (a, b) -> SubVector(a, b)),
    dotV("·", true, (a, b) -> DotProc(a, b)),
    crossV("x", (a, b) -> CrossProc(a, b), true),
    ;

    public static final VFunc[] all = values();

    public final NormalScalar op1;
    public final NormalVector op2;
    public final CrossVector op3;
    public final String symbol;
    public final Boolean scalar, cross;

    VFunc(String symbol, NormalVector opIn){
        this.symbol = symbol;
        this.op1 = null;
        this.op2 = opIn;
        this.op3 = null;
        this.scalar = false;
        this.cross = false;
    }

    VFunc(String symbol, Boolean isScalar, NormalScalar opIn){
        this.symbol = symbol;
        this.op1 = opIn;
        this.op2 = null;
        this.op3 = null;
        this.scalar = isScalar;
        this.cross = false;
    }

    VFunc(String symbol, CrossVector opIn, Boolean isCross){
        this.symbol = symbol;
        this.op1 = null;
        this.op2 = null;
        this.op3 = opIn;
        this.scalar = false;
        this.cross = isCross;
    }

    public String getSymbol() {
        return symbol;
    }

    interface CrossVector{
        double[] get(double[] a, double[] b);
    }

    interface NormalVector{
        double[] get(double[] a, double[] b);
    }

    interface NormalScalar{
        double get(double[] a, double[] b);
    }
}
