package stuff.logic;

public enum Functions {
    add("+"),
    sub("-"),
    ;

    public static final Functions[] all = Functions.values();

    public final String symbol;

    Functions(String symbol){
        this.symbol = symbol;
    }
}
