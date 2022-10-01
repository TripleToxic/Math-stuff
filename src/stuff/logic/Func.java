package stuff.logic;

import static stuff.AdditionalFunction.*;

import stuff.AdditionalFunction;

import static java.lang.Math.*;

public enum Func{
    add("+", (r1, i1, r2, i2) -> r1 + r2, (r1, i1, r2, i2) -> i1 + i2),
    sub("-", (r1, i1, r2, i2) -> r1 - r2, (r1, i1, r2, i2) -> i1 - i2),
    mul("*", (r1, i1, r2, i2) -> r1*r2 - i1*i2, (r1, r2, i1, i2) -> r1*i1 + r2*i2),
    div("/", AdditionalFunction::Rdiv, AdditionalFunction::Idiv), 
    pow("^",
    (r1, i1, r2, i2) -> pow(hypot(r1, i1), r2) * exp(-atan2(i1, r1)*i2) * cos(r2*atan2(i1, r1)+i2*log(hypot(r1, i1))), 
    (r1, i1, r2, i2) -> pow(hypot(r1, i1), r2) * exp(-atan2(i1, r1)*i2) * sin(r2*atan2(i1, r1)+i2*log(hypot(r1, i1)))), 
    ln("ln", AdditionalFunction::Rlog, AdditionalFunction::Ilog),
    sin("sin", (r, i) -> sin(r)*cosh(i), (r, i) -> sinh(i)*cos(r)),
    cos("cos", (r, i) -> cos(r)*cosh(i), (r, i) -> -sin(r)*sinh(i)),
    tan("tan",
    (r, i) -> tan(r)/(cosh(i)*cosh(i)*(1+tan(r)*tan(r)*tanh(i)*tanh(i))),
    (r, i) -> tanh(i)/(cos(r)*cos(r)*(1+tan(r)*tan(r)*tanh(i)*tanh(i)))),
    asin("asin",
    (r, i) -> PI/2d + Ilog(r + Rsqrt(r*r - i*i + 1, 2*r*i), i + Isqrt(r*r - i*i + 1, 2*r*i)),
    (r, i) -> -Rlog(r + Rsqrt(r*r - i*i + 1, 2*r*i), i + Isqrt(r*r - i*i + 1, 2*r*i))),
    acos("acos",
    (r, i) -> -Ilog(r + Rsqrt(r*r - i*i - 1, 2*r*i), i + Isqrt(r*r - i*i - 1, 2*r*i)),
    (r, i) -> Rlog(r + Rsqrt(r*r - i*i - 1, 2*r*i), i + Isqrt(r*r - i*i - 1, 2*r*i))),
    atan("atan", (r, i) -> -Ilog(Rdiv(r, i+1, -r, 1-i), Idiv(r, i+1, -r, 1-i))/2d, (r, i) -> Rlog(Rdiv(r, i+1, -r, 1-i), Idiv(r, i+1, -r, 1-i))/2d),
    cartesian("(x, y)", (r, theta) -> r*cos(theta),(r, theta) -> r*sin(theta)),
    polar("(r, θ)", (r, i) -> hypot(r, i), (r, i) -> atan2(i, r)),
    pi("π", (r) -> PI),
    e("e", (r) -> E),
    ;

    public static final Func[] all = values();

    public final Lambda1 Constants;
    public final Lambda2 SingleOutput;
    public final Lambda2 Func2;
    public final Lambda2 Func3;
    public final Lambda3 Func4;
    public final Lambda3 Func5;
    public final String symbol;
    public final boolean SingleOutputCheck, SingleInputCheck, isConstant;

    Func(String symbol, Lambda2 Func2In, Lambda2 Func3In){
        this.symbol = symbol;
        this.Constants = null;
        this.SingleOutput = null;
        this.Func2 = Func2In;
        this.Func3 = Func3In;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = false;
        this.isConstant = false;
    }

    Func(String symbol, Lambda3 Func4In, Lambda3 Func5In){
        this.symbol = symbol;
        this.Constants = null;
        this.SingleOutput = null;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = Func4In;
        this.Func5 = Func5In;
        this.SingleInputCheck = false;
        this.SingleOutputCheck = false;
        this.isConstant = false;
    }
    
    Func(String symbol, Lambda2 SingleOutputIn){
        this.symbol = symbol;
        this.Constants = null;
        this.SingleOutput = SingleOutputIn;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = true;
        this.isConstant = false;
    }

    Func(String symbol, Lambda1 ConstantsIn){
        this.symbol = symbol;
        this.Constants = ConstantsIn;
        this.SingleOutput = null;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = false;
        this.SingleOutputCheck = true;
        this.isConstant = true;
    }
    
    public String getSymbol() {
        return symbol;
    }

    interface Lambda1{
        double get(double r);
    }

    interface Lambda2{
        double get(double r, double i);
    }
    
    interface Lambda3{
        double get(double r1, double i1, double r2, double i2);
    }
}
