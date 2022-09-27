package stuff.logic;

import static java.lang.Math.*;
public enum Func{
    add("+", (r1, i1, r2, i2) -> r1 + r2, (r1, i1, r2, i2) -> i1 + i2),
    sub("-", (r1, i1, r2, i2) -> r1 - r2, (r1, i1, r2, i2) -> i1 - i2),
    mul("*", (r1, i1, r2, i2) -> r1*r2 - i1*i2, (r1, r2, i1, i2) -> r1*i1 + r2*i2),
    div("/", (r1, i1, r2, i2) -> (r1*r2 + i1*i2)/(r2*r2 + i2*i2), (r1, r2, i1, i2) -> (r2*i1 - r1*i2)/(r2*r2 + i2*i2)), 
    pow("^", 
    (r1, i1, r2, i2) -> pow(sqrt(r1*r1 + i1*i1), r2) * cos(atan2(i1, r1)*r2)  +  exp(-atan2(i1, r1)*i2) * cos(i2*log(sqrt(r1*r1 + i1*i1))), 
    (r1, i1, r2, i2) -> pow(sqrt(r1*r1 + i1*i1), r2) * sin(atan2(i1, r1)*r2)  +  exp(-atan2(i1, r1)*i2) * sin(i2*log(sqrt(r1*r1 + i1*i1)))), 
    ln("ln", (r, i) -> log(sqrt(r*r + i*i)), (r, i) -> atan2(i, r)),
    mag("dist", (r, i) -> sqrt(r*r + i*i)),
    arg("angle", (r, i) -> atan2(i, r)),
    sin("sin", (r, i) -> sin(r)*cosh(i), (r, i) -> sinh(i)*cos(r)),
    cos("cos", (r, i) -> cos(r)*cosh(i), (r, i) -> -sin(r)*sinh(i)),
    tan("tan", 
    (r, i) -> tan(r)/(cosh(i)*cosh(i)*(1+tan(r)*tan(r)*tanh(i)*tanh(i))),
    (r, i) -> tanh(i)/(cos(r)*cos(r)*(1+tan(r)*tan(r)*tanh(i)*tanh(i))));

    public static final Func[] all = values();

    public final Lambda2 SingleOutput;
    public final Lambda2 Func2;
    public final Lambda2 Func3;
    public final Lambda3 Func4;
    public final Lambda3 Func5;
    public final String symbol;
    public final boolean SingleOutputCheck, SingleInputCheck;

    Func(String symbol, Lambda2 Func2In, Lambda2 Func3In){
        this.symbol = symbol;
        this.SingleOutput = null;
        this.Func2 = Func2In;
        this.Func3 = Func3In;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = false;
    }

    Func(String symbol, Lambda3 Func4In, Lambda3 Func5In){
        this.symbol = symbol;
        this.SingleOutput = null;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = Func4In;
        this.Func5 = Func5In;
        this.SingleInputCheck = false;
        this.SingleOutputCheck = false;
    }
    
    Func(String symbol, Lambda2 SingleOutputIn){
        this.symbol = symbol;
        this.SingleOutput = SingleOutputIn;
        this.Func2 = null;
        this.Func3 = null;
        this.Func4 = null;
        this.Func5 = null;
        this.SingleInputCheck = true;
        this.SingleOutputCheck = true;
    }
    
    public String getSymbol() {
        return symbol;
    }

    interface Lambda2{
        double get(double d, double e);
    }
    
    interface Lambda3{
        double get(double r1, double i1, double r2, double i2);
    }
}
