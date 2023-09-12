package stuff.logic;

import arc.util.noise.Simplex;
import mindustry.logic.GlobalVars;

import static java.lang.Math.*;
import static stuff.util.AdditionalFunction.*;

public enum FunctionEnum {
    add("+", (a, b) -> a + b),
    sub("-", (a, b) -> a - b),
    mul("*", (a, b) -> a * b),
    div("/", (a, b) -> a / b),
    idiv("//", (a, b) -> floor(a / b)),
    mod("%", (a, b) -> a % b),
    pow("^", (a, b) -> pow(a, b)),

    equal("==", (a, b) -> abs(a - b) < 0.0000000001 ? 1 : 0),
    notEqual("not", (a, b) -> abs(a - b) < 0.0000000001 ? 0 : 1),
    land("and", (a, b) -> a != 0 && b != 0 ? 1 : 0),
    lessThan("<", (a, b) -> a < b ? 1 : 0),
    lessThanEq("<=", (a, b) -> a <= b ? 1 : 0),
    greaterThan(">", (a, b) -> a > b ? 1 : 0),
    greaterThanEq(">=", (a, b) -> a >= b ? 1 : 0),

    shl("<<", (a, b) -> (long)a << (long)b),
    shr(">>", (a, b) -> (long)a >> (long)b),

    and("b-and", (a, b) -> l(d(a) & d(b))),
    or("or", (a, b) -> l(d(a) | d(b))),
    xor("xor", (a, b) -> l(d(a) ^ d(b))),
    not("flip", a -> l(~d(a))),

    max("max", Math::max),
    min("min", Math::min),
    angle("angle", (x, y) -> atan2(y, x)),
    angleDiff("anglediff", (x, y) -> angleDist(x, y)),
    len("len", (x, y) -> hypot(x, y)),
    noise("noise", (x, y) -> Simplex.raw2d(0, x, y)),
    abs("abs", a -> abs(a)),
    log("log", Math::log),
    log10("log10", Math::log10),
    floor("floor", Math::floor),
    ceil("ceil", Math::ceil),
    sqrt("sqrt", Math::sqrt),
    cbrt("cbrt", Math::cbrt),
    rand("rand", d -> GlobalVars.rand.nextDouble() * d),

    sinF("sin", r -> sin(r)),
    cosF("cos", r -> cos(r)),
    tanF("tan", r -> tan(r)),

    asinF("asin", r -> asin(r)),
    acosF("acos", r -> acos(r)),
    atanF("atan", r -> atan(r)),

    sinh("sinh", a -> sinh(a)),
    cosh("cosh", a -> cosh(a)),
    tanh("tanh", a -> tanh(a)),

    asinh("asinh", a -> asinh(a)),
    acosh("acosh", a -> acosh(a)),
    atanh("atanh", a -> atanh(a)),

    factorial("!", a -> Factorial(a)),
    ;

    static long d(double x){
        return Double.doubleToRawLongBits(x);
    }

    static double l(long x){
        return Double.longBitsToDouble(x);
    }

    public static final FunctionEnum[] all = FunctionEnum.values();

    public final String symbol;
    public final boolean isUnary;
    public final Evaluates evals;
    public final Evaluate eval;

    FunctionEnum(String symbol, Evaluate eval){
        this.symbol = symbol;
        this.eval = eval;
        this.evals = null;
        this.isUnary = true;
    }

    FunctionEnum(String symbol, Evaluates evals){
        this.symbol = symbol;
        this.eval = null;
        this.evals = evals;
        this.isUnary = false;
    }

    @Override
    public String toString() {
        return symbol;
    }

    @FunctionalInterface
    public interface Evaluate{
        double eval(double a);
    }

    @FunctionalInterface
    public interface Evaluates{
        double eval(double a, double b);
    }
}
