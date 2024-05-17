package stuff.logic;

public enum MatrixFunc{
    Add("+"),
    Sub("-"),
    Mul("", true),
    Inverse("⁻¹"),
    Inner("•", true),
    Outer("⊗"),
    Transpose("ᵀ"),
    RowSwap("swap", true),
    ;
    
    public final String symbol;
    public final boolean different;

    public static final MatrixFunc[] all = values();

    MatrixFunc(String symbol){
        this.symbol = symbol;
        this.different = false;
    }

    MatrixFunc(String symbol, boolean diff){
        this.symbol = symbol;
        this.different = diff;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
