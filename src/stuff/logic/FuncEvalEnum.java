package stuff.logic;

public enum FuncEvalEnum {
    Eval("eval"),
    Derivative("derivative"),
    ;

    public static final FuncEvalEnum[] all = FuncEvalEnum.values();

    public final String name;

    FuncEvalEnum(String name){
        this.name = name;
    }
}
