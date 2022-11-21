package stuff.logic;

public enum AFunc{
    // Array section
    addA("+"),
    subA("-"),
    scalar("*"),
    dotProd("·"),
    crossProd("x"),
    sum("sumAll", true),
    AddEle("inclu", false, true),
    RemoveEle("exclu", false, true),
    Change("change", false, true),
    Pick("pick", false, true),
    Shift("shift", false, true),
    Shuffle("shuffle", false, true),
    // 2D Array section
    Length(true ,"length", false, true),
    ChangeR(true ,"changeR", false, true),
    AddTo(true ,"incluA", false, true),
    ChangeE(true ,"changeE", false, true),
    ;

    public static final AFunc[] all = symbolToAFunc();

    public final String symbol;
    public final boolean single, diff, RArray;

    AFunc(String symbol){
        this.symbol = symbol;
        this.single = false;
        this.diff = false;
        this.RArray = false;
    }

    AFunc(boolean RArray ,String symbol, boolean single, boolean diff){
        this.symbol = symbol;
        this.single = single;
        this.diff = diff;
        this.RArray = RArray;
    }

    AFunc(String symbol, boolean single){
        this.symbol = symbol;
        this.single = single;
        this.diff = false;
        this.RArray = false;
    }

    AFunc(String symbol, boolean single, boolean diff){
        this.symbol = symbol;
        this.single = single;
        this.diff = diff;
        this.RArray = false;
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
