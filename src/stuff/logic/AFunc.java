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

    public static final AFunc[] all = symbolToAFunc();

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

    public static AFunc[] symbolToAFunc(){
        int l = values().length;
        AFunc[] arr = new AFunc[l];
        for(int i=0; i<l; i++){
            arr[i] = AFunc.valueOf(values()[i].symbol);
        }
        return arr;
    }

}
