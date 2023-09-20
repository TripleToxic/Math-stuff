package stuff.logic;

public enum FuncEvalEnum {
    Eval("eval"),
    Integral("derivative"),
    ;

    public static final FuncEvalEnum[] all = FuncEvalEnum.values();

    public final String name;

    FuncEvalEnum(String name){
        this.name = name;
    }
}
