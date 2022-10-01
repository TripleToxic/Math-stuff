package stuff;
import static java.lang.Math.*;

public class AdditionalFunction {
    public static double asinh(double x){
        return log(x + sqrt(x*x + 1));
    }

    public static double acosh(double x){
        return log(x + sqrt(x*x - 1));
    }
}
