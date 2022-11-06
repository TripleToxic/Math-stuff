package stuff.logic;

public enum AFunc{
    addA("+"),
    subA("-"),
    Ascalar("*"),
    dotA("·"),
    crossA("x"),
    sumA("sumAll", true),
    AddElement("inclu", false, true),
    RemoveElement("exclu", false, true),
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
