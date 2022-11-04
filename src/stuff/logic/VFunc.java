package stuff.logic;

public enum VFunc {
    addV("+"),
    subV("-"),
    dotV("·"),
    crossV("x"),
    ;

    public static final VFunc[] all = values();

    public final String symbol;

    VFunc(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
