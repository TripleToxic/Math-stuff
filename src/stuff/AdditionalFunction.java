package stuff;
import static java.lang.Math.*;

import mindustry.logic.LAssembler;

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

    public static double DotProc(double[] a, double[] b){
        double dotP = 0;
        for(int i=0; i<min(a.length, b.length); i++){
            dotP += (a[i]*b[i]);
        }
        return dotP;
    }

    public static double[] AddVector(double[] a, double[] b){
        double[] A = new double[min(a.length, b.length)];
        for(int i=0; i<max(a.length, b.length); i++){
            A[i] = a[i] + b[i];
        }
        return A;
    }

    public static double[] SubVector(double[] a, double[] b){
        double[] A = new double[min(a.length, b.length)];
        for(int i=0; i<max(a.length, b.length); i++){
            A[i] = a[i] - b[i];
        }
        return A;
    }

    public static double[] CrossProc(double[] a, double[] b){
        double[] A = new double[3];
        if(min(a.length, b.length) <3){
            for(int i=0; i<3; i++){
                A[i] = a[(i+1)%3]*b[(i+2)%3] - a[(i+2)%3]*b[(i+1)%3];
            }
        }return A;

    }

    public static String[] AlphabetFunction(int n){
        String example = "abcdefghijklmnopqrstuvwxyz";
        String[] get = new String[n];
        for(int i=0; i<n; i++){
            get[i] = String.valueOf(example.charAt(i));
        }
        return get;
    }

    public static String[] Spam(int n, String p){
        String[] get = new String[n];
        for(int i=0; i<n; i++){
            get[i] = p;
        }
        return get;
    }

    public static int[] GetVars(String[] a){
        LAssembler L = new LAssembler();
        int[] K = new int[a.length];
        for(int i=0; i<a.length; i++){
            K[i] = L.var(a[i]);
        }
        return K;
    }
}
