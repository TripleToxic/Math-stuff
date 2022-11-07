package stuff;
import static java.lang.Math.*;

public class AdditionalFunction {
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

    public static double DotProd(double[] a, double[] b){
        int min = min(a.length, b.length);
        double dotP = 0;
        for(int i=0; i<min; i++){
            dotP += (a[i]*b[i]);
        }
        return dotP;
    }

    public static double[] AddVector(double[] a, double[] b){
        int min = min(a.length, b.length);
        double[] A = new double[min];
        for(int i=0; i<min; i++){
            A[i] = a[i] + b[i];
        }
        return A;
    }

    public static double[] SubVector(double[] a, double[] b){
        int min = min(a.length, b.length);
        double[] A = new double[min];
        for(int i=0; i<min; i++){
            A[i] = a[i] - b[i];
        }
        return A;
    }

    public static double[] CrossProd(double[] a, double[] b){
        double[] A = new double[3];
        for(int i=0; i<3; i++){
            if(min(a.length, b.length) >= 3){A[i] = a[(i+1)%3]*b[(i+2)%3] - a[(i+2)%3]*b[(i+1)%3];}else{A[i] = 0;}
        }
        return A;

    }

    public static String ArrToString(double[] arr){
        StringBuilder s = new StringBuilder();
        for(double i : arr){
            s.append(i).append(" ");
        }
        return s.toString();
    }

    public static double[] StringToArr(String str){
        String[] arr = str.split(" ", 0);
        double[] IA = new double[arr.length];
        for(int i=0; i<arr.length; i++){
            if(isNumeric(arr[i])){
                IA[i] = Integer.parseInt(arr[i]);
            }else{IA[i] = 0;}
        }
        return IA;
    }

    public static boolean isNumeric(String string) {             
        if(string == null || string.equals("")){
            return false;
        }
        try{
            int intValue = Integer.parseInt(string);
            return true;
        }catch (NumberFormatException e){}
        return false;
    }

    public static double sum(double[] a){
        double sum = 0;
        for(double i : a){
            sum += i;
        }
        return sum;
    }

    public static double[] prod(double[] a, double b){
        for(int i=0; i<a.length; i++){
            a[i] *= b;
        }
        return a;
    }

    public static double[] addArray(double[] arr, double a, int n){
        int c = arr.length;
        double[] narr = new double[c+1];
        if(n < 0 || n >= c+1){return arr;}
        for(int i=0; i<c+1; i++){
            if(i >= n){
                if(i == n){narr[i] = a;}else{narr[i] = arr[i-1];}
            }else{narr[i] = arr[i];}
        }
        return narr;
    }

    public static double[] removeArray(double[] arr, int n){
        int c = arr.length, a = 0;
        if(n < 0 || n >= c) return arr;
        double[] narr = new double[c-1];
        for(int i=0; i<c-1; i++){
            if(i < n){a=i;}else{a=i+1;}
            narr[i] = arr[a];
        }
        return narr;
    }

    public static double[] changeArray(double[] arr, double a, int n){
        if(n >= 0 && n < arr.length) arr[n] = a;
        return arr;
    }

    public static double pickArray(double[] arr, int n){
        if(n >= 0 && n < arr.length) return arr[n];
        return 0;
    }
}
