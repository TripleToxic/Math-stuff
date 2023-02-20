package stuff.logic;

public enum AFunc{
    // Array section
    New("new"),
    Add("+"),
    Subtract("-"),
    Muliply("*"),
    Divide("/"),
    ScalarMul("*s"),
    ScalarDiv("/s"),
    DotProd("·"),
    CrossProduct("x"),
    SumAll("sum"),
    ProductAll("prod"),
    ChangeInt("change"),
    Get("get"),
    Shuffle("shuffle"),
    Resize("resize"),
    ;

    public static final AFunc[] all = values();

    public final String symbol;

    @Override
    public String toString(){
        return symbol;
    }

    AFunc(String symbol){
        this.symbol = symbol;
    }
}
