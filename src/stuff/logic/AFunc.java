package stuff.logic;

public enum AFunc{
    // Array section
    Add("+"),
    Sub("-"),
    Mul("*"),
    DotProd("·"),
    CrossProd("x"),
    Sum("sum"),
    ProdAll("prod"),
    Change("change"),
    Get("pick"),
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
