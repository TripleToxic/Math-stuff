package stuff.logic;

import static stuff.AdditionalFunction.*;
import stuff.AdditionalFunction;

import static java.lang.Math.*;

public enum Func{
    addC("+", (r1, i1, r2, i2) -> r1 + r2, (r1, i1, r2, i2) -> i1 + i2),
    subC("-", (r1, i1, r2, i2) -> r1 - r2, (r1, i1, r2, i2) -> i1 - i2),
    mulC("*", (r1, i1, r2, i2) -> r1*r2 - i1*i2, (r1, r2, i1, i2) -> r1*i1 + r2*i2),
    divC("/", AdditionalFunction::Rdiv, AdditionalFunction::Idiv), 
    powC("^",
    (r1, i1, r2, i2) -> pow(hypot(r1, i1), r2) * exp(-atan2(i1, r1)*i2) * cos(r2*atan2(i1, r1)+i2*log(hypot(r1, i1))), 
    (r1, i1, r2, i2) -> pow(hypot(r1, i1), r2) * exp(-atan2(i1, r1)*i2) * sin(r2*atan2(i1, r1)+i2*log(hypot(r1, i1)))), 
    logC("ln", AdditionalFunction::Rlog, AdditionalFunction::Ilog),
    sinC("sin", (r, i) -> sin(r)*cosh(i), (r, i) -> sinh(i)*cos(r)),
    cosC("cos", (r, i) -> cos(r)*cosh(i), (r, i) -> -sin(r)*sinh(i)),
    tanC("tan",
    (r, i) -> tan(r)/(cosh(i)*cosh(i)*(1+tan(r)*tan(r)*tanh(i)*tanh(i))),
    (r, i) -> tanh(i)/(cos(r)*cos(r)*(1+tan(r)*tan(r)*tanh(i)*tanh(i)))),
    asinC("asin",
    (r, i) -> PI/2d + Ilog(r + Rsqrt(r*r - i*i - 1, 2*i*r), i + Isqrt(r*r - i*i - 1, 2*r*i)),
    (r, i) -> -Rlog(r + Rsqrt(r*r - i*i - 1, 2*r*i), i + Isqrt(r*r - i*i - 1, 2*r*i))),
    acosC("acos",
    (r, i) -> -Ilog(r + Rsqrt(r*r - i*i + 1, 2*r*i), i + Isqrt(r*r - i*i + 1, 2*r*i)),
    (r, i) -> Rlog(r + Rsqrt(r*r - i*i + 1, 2*r*i), i + Isqrt(r*r - i*i + 1, 2*r*i))),
    atanC("atan", (r, i) -> -Ilog(Rdiv(r, i+1, -r, 1-i), Idiv(r, i+1, -r, 1-i))/2d, (r, i) -> Rlog(Rdiv(r, i+1, -r, 1-i), Idiv(r, i+1, -r, 1-i))/2d),
    cartesian("ca<pol", (r, theta) -> r*cos(theta),(r, theta) -> r*sin(theta)),
    polar("pol<ca", (r, i) -> hypot(r, i), (r, i) -> atan2(i, r)),
    factorial("!", (r) -> Factorial(r))
    ;

    public static final Func[] all = values();

    public final Lambda1 RealFunction;
    public final Lambda2 SingleOutput, Func2, Func3;
    public final Lambda3 Func4, Func5;
    public final String symbol;
    public final boolean SingleOutputCheck, SingleInputCheck, isRealFunction;

    Func(String symbol, Lambda2 Func2In, Lambda2 Func3In){
        this.symbol = symbol;
        this.RealFunction = null;
        this.SingleOutput = null;
        this.Func2 = Func2In;
        this.Func3 = Func3In;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = false;
        this.isRealFunction = false;
    }

    Func(String symbol, Lambda3 Func4In, Lambda3 Func5In){
        this.symbol = symbol;
        this.SingleOutput = null;
        this.RealFunction = null;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = Func4In;
        this.Func5 = Func5In;
        this.SingleInputCheck = false;
        this.SingleOutputCheck = false;
        this.isRealFunction = false;
    }
    
    Func(String symbol, Lambda2 SingleOutputIn){
        this.symbol = symbol;
        this.SingleOutput = SingleOutputIn;
        this.RealFunction = null;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = true;
        this.isRealFunction = false;
    }

    Func(String symbol, Lambda1 RealFunction){
        this.symbol = symbol;
        this.SingleOutput = null;
        this.RealFunction = RealFunction;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = false;
        this.isRealFunction = true;
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
