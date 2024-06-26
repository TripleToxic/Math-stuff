package stuff.util;

import static java.lang.Math.*;

public class AdditionalFunction{
    // taylor series constants
    private static final double[] constants = {1.32934038818d, 0.934734521626d, 0.654558577981d, 0.253871246841d, 
                                         0.109673234002d, 0.0283607805948d, 0.103924034259d, 0.00160740048488d, 
                                         0.000754474128769d, 0.00000394178555927d, 0.0000613299751342d};

    private static double RationalDiscreteFactorial(double x, int term){
        double p = 1;
        boolean b = term >= 0;
        for(int i = (b ? 0 : term); i < (b ? term : 0); i++) p *= x - i;
        return b ? p : 1/p;
    }

    public static double Factorial(double x){
        int floor_x = (int)x;
        if(floor_x == x) return RationalDiscreteFactorial(0, floor_x);

        double decimal = x % 1d, part = decimal - 0.5, prod1 = part, sum = constants[0];
        
        for(int i=1; i<constants.length; i++){
            sum += prod1 * constants[i];
            prod1 *= part;
        }

        return sum / (decimal + 1) * RationalDiscreteFactorial(x, floor_x);
    }

    public static double asinh(double x){
        return log(x + sqrt(x * x + 1));
    }

    public static double acosh(double x){
        return log(x + sqrt(x * x - 1));
    }

    public static double atanh(double x){
        return 0.5 * log((1d + x) / (1d - x));
    }

    public static double angleDist(double a, double b){
        a %= 360;
        b %= 360;
        return min((a - b) < 0 ? a - b + 360 : a - b, (b - a) < 0 ? b - a + 360 : b - a);
    }

    public static double parseDouble(String s, double failed){
        try{
            return Double.parseDouble(s);
        }catch(Exception e){
            return failed;
        }
    }

    public static int parseInt(String s, int failed){
        try{
            return Integer.parseInt(s);
        }catch(Exception e){
            return failed;
        }
    }

    public static String ToString(Object[] o, int length){
        if(o == null || length < 1 || o.length == 0) return "";

        StringBuffer s = new StringBuffer(o[0].toString());
        for(int i=1; i<length; i++){
            s.append(" ").append(o[i]);
        }

        return s.toString();
    }
}