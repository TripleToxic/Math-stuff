package stuff.logic;

public enum AFunc{
    // Array section
    add("+"),
    sub("-"),
    mul("*"),
    dotProd("·"),
    crossProd("x"),
    Sum("sumAll", false),
    Change("change", true),
    Get("pick", true),
    Shuffle("shuffle", true),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final boolean diff;

    AFunc(String symbol){
        this.symbol = symbol;
        this.diff = false;
    }

    AFunc(String symbol, boolean diff){
        this.symbol = symbol;
        this.diff = diff;
    }
}
