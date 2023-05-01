package stuff.util;

public class AdditionalFunction{
    // lmao
    private static final double[] con = {1.32934038818d, 0.934734521626d, 0.654558577981d, 0.253871246841d, 
                                         0.109673234002d, 0.0283607805948d, 0.103924034259d, 0.00160740048488d, 
                                         0.000754474128769d};

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

    public static double Factorial(double x){
        int floor_x = (int)x;
        if(floor_x == x) return RationalDiscreteFactorial(0, floor_x, x >= 0);

        double part = (x - 1.5d), prod1 = part, div = 1, sum = con[0], decimal = x - floor_x;
        
        for(int i=1; i<con.length; i++){
            sum += prod1 * con[i] * div;
            
            prod1 *= (double)part;
            div /= (double)i;
        }

        return sum/x * RationalDiscreteFactorial(decimal, floor_x, x >= 0);
    }
}