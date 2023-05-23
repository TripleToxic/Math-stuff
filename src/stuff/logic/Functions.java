package stuff.logic;

import stuff.util.Function;
import static stuff.util.Function.*;

public enum Functions {
    variable("var", () -> new DVar("unused"), true),
    add("+", () -> new Add(), false),
    sub("-", () -> new Sub(), false),
    ;

    public static final Functions[] all = Functions.values();

    public final String symbol;
    public final NewFunc nf;
    public final boolean isUnary;

    Functions(String symbol, NewFunc nf, boolean isUnary){
        this.symbol = symbol;
        this.nf = nf;
        this.isUnary = isUnary;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public interface NewFunc{
        Function get();
    }
}
