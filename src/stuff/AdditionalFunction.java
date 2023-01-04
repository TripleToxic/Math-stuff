package stuff;
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
                IA[i] = Double.parseDouble(arr[i]);
            }else{IA[i] = 0;}
        }
        return IA;
    }

    public static boolean isNumeric(String string) {             
        if(string == null || string.equals("")){
            return false;
        }
        try{
            double doubleValue = Double.parseDouble(string);
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
        return (n >= 0 && n < arr.length) ? arr[n] : 0;
    }

    public static double[] shuffleArray(double[] arr){
        int l = arr.length, c = 0;
        double[] narr = new double[l];
        String[] sarr = new String[l];
        for(int i=0; i<l; i++){
            while(true){
                c = (int)(random()*l);
                if(sarr[c] == null){
                    narr[c] = arr[i];
                    sarr[c] = "Y";
                    break;
                }
            }
        }return narr;
    }

    public static double[] shiftArray(double[] arr, int n){
        int l = arr.length, k = 0;
        if(n < 0)k = l + (n % l); else k = n;
        double[] narr = new double[l];
        for(int i=0; i<l; i++){
            narr[(i + k) % l] = arr[i];
        }return narr;
    }

    public static double[][] ChangeRArrayE(double[][] arrR, double a, int column, int row){
        if((column >= 0 && arrR.length > column) && (row >= 0 && arrR[0].length > row)) arrR[column][row] = a;
        return arrR;
    }

    public static double[][] addArrayToRArray(double[][] arrR, double[] arr, int n){
        int column = arrR.length, length = arr.length, maxRows = max(arrR[0].length, length), remainder = 0;
        double[][] nRectArr = new double[column+1][maxRows];
        if(n < 0 || n >= column+1){return arrR;}
        for(int i=0; i<column+1; i++){
            remainder = i != n ? arrR[i - i >= n ? 1:0].length : length;
            for(int j=0; j<maxRows; j++){
                nRectArr[i][j] = j < remainder ? (i != n ? arrR[i - i >= n ? 1:0][j] : arr[j]) : 0;
            }
        }
        return nRectArr;
    }

    public static double[][] changeRArray(double[][] arrR, double[] arr, int n){
        if(n >= 0 && n < arrR.length) arrR[n] = arr.clone();
        return arrR;
    }

    public static double[][] NewRArray(int column, int row){
        return new double[column][row];
    }

    public static double[][] LimitR(double[][] RArr){
        int C = RArr.length, R = RArr[0].length;
        if(C <= limit && R <= limit){return RArr;}
        double[][] LRArr = new double[limit][limit];
        for(int i=0; i<limit; i++){
            for(int j=0; j<limit; j++){
                LRArr[i][j] = RArr[i][j];
            }
        }
        return LRArr;
    }

    public static double[] Limit(double[] Arr){
        int L = Arr.length;
        if(L <= limit){return Arr;}
        double[] LArr = new double[limit];
        for(int i=0; i<limit; i++){
            LArr[i] = Arr[i];
        }
        return LArr;
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