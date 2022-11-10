package stuff.logic;

public enum AFunc{
    addA("+"),
    subA("-"),
    Ascalar("*"),
    dotProd("·"),
    crossProd("x"),
    sumA("sumAll", true),
    AddEle("inclu", false, true),
    RemoveEle("exclu", false, true),
    Change("change", false, true),
    Pick("pick", false, true),
    Shift("shift", false, true),
    Shuffle("shuffle", false, true),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final boolean single, diff;

    AFunc(String symbol){
        this.symbol = symbol;
        this.single = false;
        this.diff = false;
    }

    AFunc(String symbol, boolean single){
        this.symbol = symbol;
        this.single = single;
        this.diff = false;
    }

    AFunc(String symbol, boolean single, boolean diff){
        this.symbol = symbol;
        this.single = single;
        this.diff = diff;
    }
}
