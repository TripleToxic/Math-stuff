package stuff.logic;

public enum VFunc {
    addV("+"),
    subV("-"),
    dotV("·"),
    crossV("x"),
    ;

    public static final VFunc[] all = values();

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

    public String getSymbol() {
        return symbol;
    }
}
