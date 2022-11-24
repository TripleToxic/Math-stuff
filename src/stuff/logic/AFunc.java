package stuff.logic;

public enum AFunc{
    // Array section
    addA("+"),
    subA("-"),
    scalar("*"),
    dotProd("·"),
    crossProd("x"),
    sum("sumAll", false),
    AddEle("inclu", true),
    RemoveEle("exclu", true),
    Change("change", true),
    Pick("pick", true),
    Shift("shift", true),
    Shuffle("shuffle", true),
    // 2D Array sectio
    Length("length", true),
    ChangeR("changeR", true),
    AddTo("incluA", true),
    ChangeE("changeE", true),
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
