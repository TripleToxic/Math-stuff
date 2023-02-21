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
    Get("get", "number", "array"),
    Shuffle("shuffle"),
    Resize("resize"),
    ;

    public static final AFunc[] all = values();

    public final String symbol;
    public final String[] choice;

    @Override
    public String toString(){
        return symbol;
    }

    AFunc(String symbol){
        this.symbol = symbol;
        this.choice = null;
    }

    AFunc(String symbol, String... choice){
        this.symbol = symbol;
        this.choice = choice.clone();
    }
}
