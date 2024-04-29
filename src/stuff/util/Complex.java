package stuff.util;

/** 
 * Operations with complex number, returning a Complex type for chaining
*/
public class Complex{
    public static final Complex real = new Complex(1, 0),
                                imaginary = new Complex(0, 1);

    public double r = 0, i = 0;
    public boolean unchecked = true;
    
    public Complex(){}

    public Complex(double x, double y){
        set(x, y);
    }

    public Complex(Complex c){
        set(c);
    }

    public Complex set(Complex c){
        r = c.r;
        i = c.i;
        return this;
    }

    public Complex set(double x, double y){
        r = x;
        i = y;
        return this;
    }

    public Complex Default(){
        return set(0, 0);
    }

    public double length(){
        if(r == 0) return Math.abs(i);
        if(i == 0) return Math.abs(r);
        return Math.hypot(r, i);
    }

    public double Angle(){
        return Math.atan2(i, r);
    }

    public static Complex add(Complex c1, Complex c2, Complex c3){
        c3.r = c1.r + c2.r;
        c3.i = c1.r + c2.r;
        return c3;
    }

    public Complex add(double x, double y){
        r += x;
        i += y;
        return this;
    }

    public static Complex sub(Complex c1, Complex c2, Complex c3){
        c3.r = c1.r - c2.r;
        c3.i = c1.r - c2.r;
        return c3;
    }

    public Complex sub(double x, double y){
        r -= x;
        i -= y;
        return this;
    }

    /**
     * @param n1 determine if the real part can be negative
     * @param n2 determine if the imaginary part can be negative
     * @return a complex number that has one or all component(s) become negative
     */
    public Complex negate(boolean n1, boolean n2){
        r = n1 ? -r : r;
        i = n2 ? -i : i;
        return this;
    }

    /**
     * @return A complex number with its components swapped
     */
    public Complex swap(){
        double a = r;
        r = i;
        i = a;
        return this;
    }

    public static  Complex mul(Complex c1, double x, Complex c2){
        c2.r = c1.r * x;
        c2.i = c1.i * x;
        return c2;
    }

    public static Complex mul(Complex c1, Complex c2, Complex c3){
        double a = c1.i * c2.r + c1.r * c2.i;
        c3.r = c1.r * c2.r - c1.i * c2.i;
        c3.i = a;
        return c3;
    }

    public static Complex div(Complex c1, double x, Complex c2){
       return mul(c1, 1/x, c2);
    }

    public static Complex div(Complex c1, Complex c2, Complex c3){
        double div = 1.0 / (c2.r*c2.r + c2.i*c2.i), 
               a   = (c1.i * c2.r - c1.r * c2.i) * div;

        c3.r = (c1.r * c2.r + c1.i * c2.i) * div;
        c3.i = a;
        return c3;
    }

    public static Complex square(Complex c1, Complex c2){
        double a = 2d*c1.r*c1.i;
        c2.r = c1.r*c1.r + c1.i*c1.i;
        c1.i = a;
        return c2;
    }

    public static Complex complexToPolar(Complex c1, Complex c2){
        double a = c1.Angle();

        c2.r = c1.length();
        c2.i = a;
        return c2;
    }

    public static Complex polarToComplex(Complex c1, Complex c2){
        double a = c1.Angle(),
               b = c1.length();
        c2.r = Math.cos(a);
        c2.i = Math.sin(a);
        return mul(c2, b, c2);
    }

    public static Complex pow(Complex c1, Complex c2, Complex c3){
        double var1 = c1.length(), 
               var2 = c1.Angle(), 
               
               var5 = 1, var6 = 0;

        if(c2.i != 0d){
            var5 = Math.exp(-var2 * c2.i);
            var6 = c2.i * Math.log(var1);

            if(c2.r == 0) {
                c3.r = var5 * Math.cos(var6);
                c3.i = var5 * Math.sin(var6);
                return c3;
            }
        }

        if(c2.r == 0) return real;
        if(c2.r == 1) return c3.set(c1);
        if(c2.r == 2) return square(c1, c3);

        double var3 = Math.pow(var1, c2.r),
               var4 = c2.r * var1;

        var3 *= var5;
        var4 += var6;

        c3.r = var3 * Math.cos(var4);
        c3.i = var3 * Math.sin(var4);
        return c3;
    }

    /**
     * 
     * @return The square root of a complex number with special cases
     */

    public static Complex sqrt(Complex c1, Complex c2){
        if(c1.i == 0) {
            if(c1.r >= 0) c2.r = Math.sqrt(c1.r);
            else c2.i = Math.sqrt(c1.r);
        }else if(c1.r == 0){
            double var = 0.5d * Math.sqrt(2d * Math.abs(c1.i));

            if(c1.i >= 0){
                c2.r = c2.i = var;
            }else{
                c2.r = -var;
                c2.i = var;
            }
        }else{
            double var = Math.sqrt(0.5d * (c1.length() - c1.r));
            c2.r = 0.5d * c1.i/var;
            c2.i = var;
        }
        return c2;
    }

    /**
     * @return The natural log of a complex number
     */
    public static Complex log(Complex c1, Complex c2){
        if(c1.i == 0){
            c2.r = Math.log(c1.r);
        }else if(c1.r == 0){
            c2.r = Math.log(c1.i);
            c2.i = 0.5 * Math.PI;
        }else{
            double a = c1.Angle();
            c2.r = Math.log(c1.length());
            c2.i = a;
        }
        return c2;
    }

    public static Complex sin(Complex c1, Complex c2){
        if(c1.i == 0){
            c2.r = Math.sin(c1.r);
        }else if(c1.r == 0){
            c2.i = Math.sinh(c1.i);
        }else{
            double a = Math.cos(c1.r) * Math.sinh(c1.i);
            c2.r = Math.sin(c1.r) * Math.cosh(c1.i);
            c2.i = a;
        }
        return c2;
        
    }

    public static Complex cos(Complex c1, Complex c2){
        if(c1.i == 0){
            c2.r = Math.cos(c1.r);
        }else if(c1.r == 0){
            c2.r = Math.cosh(c1.i);
            c2.i = 0;
        }else{
            double a = -Math.sin(c1.r) * Math.sinh(c1.i);
            c2.r = Math.cos(c1.r) * Math.cosh(c1.i);
            c2.i = a;
        }
        return c2;
    }

    public static Complex tan(Complex c1, Complex c2){
        if(c1.i == 0){
            c2.r = Math.tan(c1.r);
        }else if(c1.r == 0){
            c2.i = Math.tanh(c1.i);
        }else{
            double var1 = 2 * c1.r,
                var2 = 2 * c1.i,

                var3 = Math.cos(var1) + Math.cosh(var2);
            c2.r = Math.sin(var1);
            c2.i = Math.sinh(var2);
            div(c2, var3, c2);
        }
        return c2;
    }

    public static Complex asin(Complex c1, Complex c2){
        sub(real, square(c1, c2), c2);sqrt(c2, c2).add(c1.i, c1.r);return log(c2, c2).swap().negate(false, true);
    }

    public static Complex acos(Complex c1, Complex c2){
        sub(square(c1, c2), real, c2);sqrt(c2, c2).add(c1.r, c1.i);return log(c2, c2).swap().negate(false, true);
    }

    public static Complex atan(Complex c1, Complex c2){
        Complex c = new Complex(c1);
        sub(c1, imaginary, c2);div(c2, sub(c1, imaginary, c), c2);log(c2, c2).swap().negate(true, false); return mul(c2, 0.5d, c2);
    }
}
