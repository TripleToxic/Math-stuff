package stuff;
import static java.lang.Math.*;

public class AdditionalFunction {
    /** arc sinh of a variable x */
    public static double asinh(double x){
        return log(x + sqrt(x*x + 1));
    }
    /** arc cosh of a variable x */
    public static double acosh(double x){
        return log(x + sqrt(x*x - 1));
    }

    public static double Rsqrt(double r, double i){
        return sqrt(hypot(r, i))*cos(atan2(i, r)/2d);
    }

    public static double Isqrt(double r, double i){
        return sqrt(hypot(r, i))*sin(atan2(i, r)/2d);
    }

    public static double Rlog(double r, double i){
        return log(hypot(r, i));
    }

    public static double Ilog(double r, double i){
        return atan2(i, r);
    }

    public static double Rdiv(double r1, double i1, double r2, double i2){
        return (r1*r2 + i1*i2)/(r2*r2 + i2*i2);
    }

    public static double Idiv(double r1, double i1, double r2, double i2){
        return (r2*i1 - r1*i2)/(r2*r2 + i2*i2);
    }

    public static double RCexpR(double r, double i, double b){
        return pow(hypot(r, i), b)*cos(b*atan2(i, r));
    }

    public static double ICexpR(double r, double i, double b){
        return pow(hypot(r, i), b)*sin(b*atan2(i, r));
    }
}
