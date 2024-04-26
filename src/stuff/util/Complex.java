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

    public Complex add(Complex c1, Complex c2){
        r = c1.r + c2.r;
        i += c1.r + c2.r;
        return this;
    }

    public Complex add(double x, double y){
        r += x;
        i += y;
        return this;
    }

    public Complex sub(Complex c1, Complex c2){
        r = c1.r - c2.r;
        i = c1.r - c2.r;
        return this;
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

    public Complex mul(Complex c, double x){
        r = c.r * x;
        i = c.i * x;
        return this;
    }

    public Complex mul(Complex c1, Complex c2){
        double a = c1.i * c2.r + c1.r * c2.i;
        r = c1.r * c2.r - c1.i * c2.i;
        i = a;
        return this;
    }

    public Complex div(Complex c, double x){
       return this.mul(c, 1/x);
    }

    public Complex div(Complex c1, Complex c2){
        double div = 1.0 / (c2.r*c2.r + c2.i*c2.i), 
               a   = (c1.i * c2.r - c1.r * c2.i) * div;

        r = (c1.r * c2.r + c1.i * c2.i) * div;
        i = a;
        return this;
    }

    public Complex square(Complex c){
        r = c.r*c.r + c.i*c.i;
        i = 2d*c.r*c.i;
        return this;
    }

    public Complex complexToPolar(Complex c){
        double a = c.Angle();

        r = c.length();
        i = a;
        return this;
    }

    public Complex polarToComplex(Complex c){
        double a = c.Angle(),
               b = c.length();
        r = Math.cos(a);
        i = Math.sin(a);
        return this.mul(this, b);
    }

    public Complex pow(Complex c1, Complex c2){
        double var1 = c1.length(), 
               var2 = c1.Angle(), 
               
               var5 = 1, var6 = 0;

        if(c2.i != 0d){
            var5 = Math.exp(-var2 * c2.i);
            var6 = c2.i * Math.log(var1);

            if(c2.r == 0) {
                r = var5 * Math.cos(var6);
                i = var5 * Math.sin(var6);
                return this;
            }
        }

        if(c2.r == 0) return real;
        if(c2.r == 1) return this;
        if(c2.r == 2) return square(c1);

        double var3 = Math.pow(var1, c2.r),
               var4 = c2.r * var1;

        var3 *= var5;
        var4 += var6;

        r = var3 * Math.cos(var4);
        i = var3 * Math.sin(var4);
        return this;
    }

    /**
     * 
     * @return The square root of a complex number with special cases
     */

    public Complex sqrt(Complex c){
        if(i == 0) {
            if(c.r >= 0) r = Math.sqrt(c.r);
            else i = Math.sqrt(c.r);
        }else if(r == 0){
            double var = 0.5d * Math.sqrt(2d * Math.abs(c.i));

            if(i >= 0){
                r = i = var;
            }else{
                r = -var;
                i = var;
            }
        }else{
            double var = Math.sqrt(0.5d * (length() - c.r));
            r = 0.5d * i/var;
            i = var;
        }
        return this;
    }

    /**
     * @return The natural log of a complex number
     */
    public Complex log(Complex c){
        if(i == 0){
            r = Math.log(c.r);
        }else if(r == 0){
            r = Math.log(c.i);
            i = 0.5 * Math.PI;
        }else{
            double a = c.Angle();
            r = Math.log(c.length());
            i = a;
        }
        return this;
    }

    public Complex sin(Complex c){
        if(i == 0){
            r = Math.sin(c.r);
        }else if(r == 0){
            i = Math.sinh(c.i);
        }else{
            double a = Math.cos(c.r) * Math.sinh(c.i);
            r = Math.sin(c.r) * Math.cosh(c.i);
            i = a;
        }
        return this;
        
    }

    public Complex cos(Complex c){
        if(i == 0){
            r = Math.cos(c.r);
        }else if(r == 0){
            r = Math.cosh(c.i);
        }else{
            double a = -Math.sin(c.r) * Math.sinh(c.i);
            r = Math.cos(c.r) * Math.cosh(c.i);
            i = a;
        }
        return this;
    }

    public Complex tan(Complex c){
        if(i == 0){
            r = Math.tan(c.r);
        }else if(r == 0){
            i = Math.tanh(c.i);
        }else{
            double var1 = 2 * c.r,
                var2 = 2 * c.i,

                var3 = Math.cos(var1) + Math.cosh(var2);
            r = Math.sin(var1);
            i = Math.sinh(var2);
            this.div(this, var3);
        }
        return this;
    }

    public Complex asin(Complex c){
        set(c);
        return sub(real, square(c)).sqrt(this).add(c.i, c.r).log(this).swap().negate(false, true);
    }

    public Complex acos(Complex c){
        set(c);
        return sub(square(this), real).sqrt(this).add(c.r, c.i).log(this).swap().negate(false, true);
    }

    public Complex atan(Complex c){
        set(c);
        return this.add(imaginary, c).div(this, sub(imaginary, this)).log(this).swap().negate(true, false).mul(this, 0.5d);
    }
}
