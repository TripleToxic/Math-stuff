package stuff.logic;

public enum MatrixFunc{
    Add("+"),
    Sub("-"),
    Mul(""),
    Inverse("⁻¹"),
    Inner("•"),
    Outer("⊗"),
    Transpose("ᵀ"),
    RowAdd("row+"),
    RowSub("row-"),
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
