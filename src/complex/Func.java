package complex;

public enum Func
{
    add("+", (r1, i1, r2, i2) -> r1 + r2, (r1, i1, r2, i2) -> i1 + i2),
    sub("-", (r1, i1, r2, i2) -> r1 - r2, (r1, i1, r2, i2) -> i1 - i2),
    mul("*", (r1, i1, r2, i2) -> r1*r2 - i1*i2, (r1, r2, i1, i2) -> r1*i1 + r2*i2),
    div("/", (r1, i1, r2, i2) -> (r1*r2 + i1*i2)/(r2*r2 + i2*i2), (r1, r2, i1, i2) -> (r2*i1 - r1*i2)/(r2*r2 + i2*i2)), 
    pow("^", 
    (r1, i1, r2, i2) -> Math.pow(Math.sqrt(r1*r1 + i1*i1), r2) * Math.cos(Math.atan2(i1, r1)*r2)  +  Math.exp(-Math.atan2(i1, r1)*i2) * Math.cos(i2*Math.log(Math.sqrt(r1*r1 + i1*i1))), 
    (r1, i1, r2, i2) -> Math.pow(Math.sqrt(r1*r1 + i1*i1), r2) * Math.sin(Math.atan2(i1, r1)*r2)  +  Math.exp(-Math.atan2(i1, r1)*i2) * Math.sin(i2*Math.log(Math.sqrt(r1*r1 + i1*i1)))), 
    ln("ln", (r, i) -> Math.log(Math.sqrt(r*r + i*i)), (r, i) -> Math.atan2(i, r)),
    mag("dist", (r, i) -> Math.sqrt(r*r + i*i)),
    arg("angle", (r, i) -> Math.atan2(i, r)),
    sin("sin", (r, i) -> Math.sin(r)*Math.cosh(i), (r, i) -> Math.sinh(i)*Math.cos(r)),
    cos("cos", (r, i) -> Math.cos(r)*Math.cosh(i), (r, i) -> -Math.sin(r)*Math.sinh(i)),
    tan("tan", 
    (r, i) -> Math.tan(r)/(Math.cosh(i)*Math.cosh(i)*(1+Math.tan(r)*Math.tan(r)*Math.tanh(i)*Math.tanh(i))),
    (r, i) -> Math.tanh(i)/(Math.cos(r)*Math.cos(r)*(1+Math.tan(r)*Math.tan(r)*Math.tanh(i)*Math.tanh(i))));

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
        double get(double r, double i);
    }
    
    interface Lambda3{
        double get(double r1, double i1, double r2, double i2);
    }
}
