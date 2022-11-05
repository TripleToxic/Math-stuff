package stuff.logic;

public enum AFunc{
    addA("+"),
    subA("-"),
    Ascalar("*"),
    dotA("·"),
    crossA("x"),
    sumA("Σ", true),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final boolean single;

    AFunc(String symbol){
        this.symbol = symbol;
        this.single = false;
    }

    AFunc(String symbol, boolean single){
        this.symbol = symbol;
        this.single = single;
    }

    public String getSymbol() {
        return symbol;
    }
}
