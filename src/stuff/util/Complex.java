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

    public Complex add(Complex c){
        r += c.r;
        i += c.i;
        return this;
    }

    public Complex add(double x, double y){
        r += x;
        i += y;
        return this;
    }

    public Complex sub(Complex c){
        r -= c.r;
        i -= c.i;
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

    public Complex mul(double x){
        r *= x;
        i *= x;
        return this;
    }

    public Complex mul(Complex c){
        double a = r * c.r - i * c.i,
               b = i * c.r + r * c.i;

        r = a;
        i = b;
        return this;
    }

    public Complex div(double x){
       return this.mul(1/x);
    }

    public Complex div(Complex c){
        double div = 1 / (c.r*c.r + c.i*c.i),
                 a = (r * c.r + i * c.i) * div,
                 b = (i * c.r - r * c.i) * div;

        r = a;
        i = b;
        return this;
    }

    public Complex square(){
        double a = r*r + i*i,
               b = 2d*r*i;
        r = a;
        i = b;
        return this;
    }

    public Complex complexToPolar(){
        double a = length(),
               b = Angle();
        r = a;
        i = b;
        return this;
    }

    public Complex polarToComplex(){
        double a = Angle(),
               b = length();
        r = Math.cos(a);
        i = Math.sin(b);
        return this.mul(b);
    }

    public Complex pow(Complex c){
        double var1 = length(), 
               var2 = Angle(), 
               
               var5 = 1, var6 = 0;

        if(c.i != 0d){
            var5 = Math.exp(-var2 * c.i);
            var6 = c.i * Math.log(var1);

            if(c.r == 0) {
                r = var5 * Math.cos(var6);
                i = var5 * Math.sin(var6);
                return this;
            }
        }

        if(c.r == 0) return real;
        if(c.r == 1) return this;
        if(c.r == 2) return square();

        double var3 = Math.pow(var1, c.r),
               var4 = c.r * var1;

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

    public Complex sqrt(){
        if(i == 0) {
            if(r >= 0) r = Math.sqrt(r);
            else i = Math.sqrt(r);
        }else if(r == 0){
            double var1 = 0.5d * Math.sqrt(2d * Math.abs(i));

            if(i >= 0){
                r = i = var1;
            }else{
                r = -var1;
                i = var1;
            }
        }else{
            double var1 = Math.sqrt(0.5d * (length() - r));
            r = 0.5d * i/var1;
            i = var1;
        }
        return this;
    }

    /**
     * @return The natural log of a complex number
     */
    public Complex log(){
        if(i == 0){
            r = Math.log(r);
        }else if(r == 0){
            r = Math.log(i);
            i = 0.5 * Math.PI;
        }else{
            double a = Math.log(length()),
                b = Angle();
            r = a;
            i = b;
        }
        return this;
    }

    public Complex sin(){
        if(i == 0){
            r = Math.sin(r);
        }else if(r == 0){
            i = Math.sinh(i);
        }else{
            double a = Math.sin(r) * Math.cosh(i), 
                b = Math.cos(r) * Math.sinh(i);
            r = a;
            i = b;
        }
        return this;
        
    }

    public Complex cos(){
        if(i == 0){
            r = Math.cos(r);
        }else if(r == 0){
            r = Math.cosh(i);
            i = 0;
        }else{
            double a = Math.cos(r) * Math.cosh(i),
                b = -Math.sin(r) * Math.sinh(i);
            r = a;
            i = b;
        }
        return this;
    }

    public Complex tan(){
        if(i == 0){
            r = Math.tan(r);
        }else if(r == 0){
            i = Math.tanh(i);
        }else{
            double var1 = 2 * r,
                var2 = 2 * i,

                var3 = Math.cos(var1) + Math.cosh(var2);
            r = Math.sin(var1);
            i = Math.sinh(var2);
            this.div(var3);
        }
        return this;
    }

    public Complex asin(){
        return real.sub(square()).sqrt().add(-i, r).log().swap().negate(false, true);
    }

    public Complex acos(){
        return square().sub(real).sqrt().add(r, i).log().swap().negate(false, true);
    }

    public Complex atan(){
        return this.add(imaginary).div(imaginary.sub(this)).log().swap().negate(true, false).mul(0.5d);
    }
}
