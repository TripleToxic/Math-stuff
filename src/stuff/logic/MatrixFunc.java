package stuff.logic;

public enum MatrixFunc{
    Get("get"),
    Add("+"),
    Sub("-"),
    Mul(""),
    Inverse("⁻¹"),
    Inner("•"),
    Outer("⊗"),
    Transpose("ᵀ"),
    RowSwap("swap"),
    ;

    public final String symbol;

    MatrixFunc(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static final MatrixFunc[] all = values();
}
