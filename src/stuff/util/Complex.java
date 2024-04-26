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
        i += c1.r - c2.r;
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
        r = c1.r * c2.r - c1.i * c2.i;
        i = c1.i * c2.r + c1.r * c2.i;
        return this;
    }

    public Complex div(Complex c, double x){
       return this.mul(c, 1/x);
    }

    public Complex div(Complex c1, Complex c2){
        double div = 1.0 / (c2.r*c2.r + c2.i*c2.i);

        r = (c1.r * c2.r + c1.i * c2.i) * div;
        i = (c1.i * c2.r - c1.r * c2.i) * div;
        return this;
    }

    public Complex square(Complex c){
        r = c.r*c.r + c.i*c.i;
        i = 2d*c.r*c.i;
        return this;
    }

    public Complex complexToPolar(Complex c){
        double a = c.length(),
               b = c.Angle();
        r = a;
        i = b;
        return this;
    }

    public Complex polarToComplex(Complex c){
        double a = c.Angle(),
               b = c.length();
        r = Math.cos(a);
        i = Math.sin(a);
        return this.mul(this, b);
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
    public Complex log(Complex c){
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

    public Complex asin(Complex c){
        set(c);
        return real.sub(square()).sqrt().add(c.i, c.r).log().swap().negate(false, true);
    }

    public Complex acos(Complex c){
        set(c);
        return square().sub(real).sqrt().add(c.r, c.i).log().swap().negate(false, true);
    }

    public Complex atan(Complex c){
        set(c);
        return this.add(imaginary, c).div(imaginary.sub(this)).log().swap().negate(true, false).mul(0.5d);
    }
}
