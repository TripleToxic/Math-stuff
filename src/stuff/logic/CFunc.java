package stuff.logic;

import stuff.util.AdditionalFunction;
import stuff.util.*;

public enum CFunc{
    New("new"),
    get("extract"),
    addC("+", (Complex c1,  Complex c2) -> c1.add(c2)),
    subC("-", (Complex c1, Complex c2) -> c1.sub(c2)),
    mulC("*", (Complex c1, Complex c2) -> c1.mul(c2)),
    divC("/", (Complex c1, Complex c2) -> c1.div(c2)), 
    powC("^", (Complex c1, Complex c2) -> c1.pow(c2)), 
    logC("ln", Complex::log),
    sinC("sin", Complex::sin),
    cosC("cos", Complex::cos),
    tanC("tan", Complex::tan),
    asinC("asin", Complex::asin),
    acosC("acos", Complex::acos),
    atanC("atan", Complex::atan),
    cartesian("complex", Complex::polarToComplex),
    polar("polar", Complex::complexToPolar),
    factorial("!", AdditionalFunction::Factorial)
    ;

    public static final CFunc[] all = values();

    public final String symbol;
    public Lambda1 RealFunction = null;
    public Lambda2 Unary = null;
    public Lambda3 Binary = null;
    public boolean unary = false, 
                   binary = false, 
                   real = false;
    
    CFunc(String symbol){this.symbol = symbol;}

    CFunc(String symbol, Lambda1 f){
        this.symbol = symbol;
        this.RealFunction = f;
        this.real = true;
    }

    CFunc(String symbol, Lambda2 f){
        this.symbol = symbol;
        this.Unary = f;
        this.unary = true;
    }

    CFunc(String symbol, Lambda3 f){
        this.symbol = symbol;
        this.Binary = f;
        this.binary = true;
    }
    
    @Override
    public String toString() {
        return symbol;
    }

    interface Lambda1{
        double get(double r);
    }

    interface Lambda2{
        Complex get(Complex c);
    }
    
    interface Lambda3{
        Complex get(Complex c1, Complex c2);
    }
}
