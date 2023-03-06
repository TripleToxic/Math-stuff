package stuff.util;
import static java.lang.Math.*;

public class AdditionalFunction{
    public static int limit = 20, iter = 10000;

    /** @return arc sinh of x*/
    public static double asinh(double x){
        return log(x + sqrt(x*x + 1));
    }
    /** @return arc cosh of x*/
    public static double acosh(double x){
        return log(x + sqrt(x*x - 1));
    }
    /** @return real part of the sqrt function of a complex number*/
    public static double Rsqrt(double r, double i){
        return sqrt(hypot(r, i))*cos(atan2(i, r)/2d);
    }
    /** @return imaginary part of the sqrt function of a complex number*/
    public static double Isqrt(double r, double i){
        return sqrt(hypot(r, i))*sin(atan2(i, r)/2d);
    }
    /** @return real part of the natural log function of a complex number*/
    public static double Rlog(double r, double i){
        return log(hypot(r, i));
    }
    /** @return imaginary part of the natural log function of a complex number*/
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

    private static double RationalDiscreteFactorial(double decimal, int term, boolean x_is_positive){
        double p = 1;
        decimal %= 1;
        if(x_is_positive){
            for(int i=0; i<term; i++){
                p *= term - i + decimal;
            }
        }else{
            p /= decimal;
            for(int i=-1; i>term; i--){
                p /= term - i  + decimal;
            }
        }
        return p;
    }

    private static double G(double x, double t){
        return pow(t, x) * exp(-t);
    }

    public static double Factorial(double x){
        int upper_bound = 30, floor_x = (int)floor(x);
        double dt = (double)upper_bound/iter, step2 = 0, decimal = x - floor_x, sum = 0, h = dt/8d;
        if(decimal != 0){
            for(int n=0; n<iter; n++){
                step2 = n*dt;
                sum += h*(G(decimal, step2) + 3*G(decimal, step2 + dt/3d) + 3*G(decimal, step2 + 2*dt/3d) + G(decimal, step2 + dt));
            }
        }else{sum = 1;}
        return sum * RationalDiscreteFactorial(decimal, floor_x, x >= 0);
    }
}