package stuff.logic;

public enum AFunc{
    // Array section
    New("new"),
    Add("+"),
    Subtract("-"),
    Muliply("*"),
    Divide("/"),
    DotProd("·", false, true),
    CrossProduct("x"),
    SumAll("sum", false, true),
    ProductAll("prod", false, true),
    Change("change", true),
    Get("get", false, true),
    Shuffle("shuffle", true),
    Resize("resize"),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final boolean local, number;

    @Override
    public String toString(){
        return symbol;
    }

    AFunc(String symbol){
        this.symbol = symbol;
        this.local = false;
        this.number = false;
    }

    AFunc(String symbol, boolean local){
        this.symbol = symbol;
        this.local = local;
        this.number = false;
    }

    AFunc(String symbol, boolean local, boolean number){
        this.symbol = symbol;
        this.local = local;
        this.number = number;
    }

    public static enum TwoType{
        number,
        array;

        public static final TwoType[] all = values();
    }
}
