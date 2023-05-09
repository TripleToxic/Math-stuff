package stuff.util;

import arc.math.Mathf;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Math.*;

public class Array{
    private int[] DefaultLength = {0, 0, 1};
    public int[] l;
    public double[] s;
    public int All;
    private static int length_limit = 16;

    public Array(){
        this.l = DefaultLength;
        this.s = new double[1];
        this.All = 1;
    }

    public Array(int... arr){
        Limit(arr);
        this.l = arr;
        int f = productAll(this.l);
        this.s = new double[f];
        this.All = f;
    }

    public Array(double... arr){
        int[] a = {1, 1, arr.length};
        this.l = a;
        this.s = arr;
        this.All = productAll(this.l);
    }

    private static int[] NumToPos(int[] arr, int num){
        int l2 = arr.length, buffer = 1;
        int[] count_arr = new int[l2];
        for(int j=l2-1; j>=0; j--){
            count_arr[j] = num/buffer % arr[j];
            buffer *= arr[j];
        }
        return count_arr;
    }

    public static void Limit(int[] i){
        for(int n=0; n<3; n++){
            i[n] = Mathf.clamp(i[n], 1, length_limit);
        }
    }

    public static int productAll(int[] i){
        int d = 1;
        for (int j : i) {
            d *= j;
        }return d;
    }

    public static double productAll(double[] i){
        double d = 1;
        for (double j : i) {
            d *= j;
        }return d;
    }

    public double getNum(int[] pos){
        int s_pos = 0, buffer = 1;
        for(int i=pos.length-1; i>=0; i--){
            if(pos[i] < l[i]) s_pos += pos[i] * buffer;
            else return 0;
            buffer *= l[i];
        }return s[s_pos];
    }

    public double sumAll(){
        double sum = 0;
        for(double i : s){
            sum += i;
        }
        return sum;
    }

    public void prod(double b){
        for(int i=0; i<s.length; i++){
            s[i] *= b;
        }
    }

    public void div(double b){
        for(int i=0; i<s.length; i++){
            s[i] /= b;
        }
    }

    public void add(Array b){
        for(int i=0; i<All; i++){
            s[i] += b.getNum(NumToPos(l, i));
        }
    }

    public void minus(Array b){
        for(int i=0; i<All; i++){
            s[i] -= b.getNum(NumToPos(l, i));
        }
    }

    public void prodEach(Array b){
        for(int i=0; i<All; i++){
            s[i] *= b.getNum(NumToPos(l, i));
        }
    }

    public void divEach(Array b){
        for(int i=0; i<All; i++){
            s[i] /= b.getNum(NumToPos(l, i));
        }
    }

    public double dotProd(Array b){
        double h = 0;
        for(int i=0; i<min(l[2], b.l[2]); i++){
            h += s[i] * b.s[i];
        }return h;
    }

    // a
    /*
    public Array ArrMultiplication(Array b){
        if(l.length > 2 || b.l.length > 2) return new ArrayStringDouble("none");
        if(l.length == 1 && b.l.length == 1) return new ArrayStringDouble("none");
        int buffer1 = min(l[0], b.l[1]);
        int[] buffer2 = {l[1], b.l[0]};
        ArrayStringDouble out = new ArrayStringDouble(buffer2);
        for(int i=0; i<l[1]; i++){
            for(int j=0; j<b.l[0]; j++){
                
            }
        }
    }
    */

    public Array crossProd(Array b){
        Array c = new Array();
        if(l.length != 1 && b.l.length != 1 && l[0] < 3 && b.l[0] < 3) return c;
        for(int i=0; i<3; i++){
            c.s[i] = s[(i+1)%3]*b.s[(i+2)%3] - s[(i+2)%3]*b.s[(i+1)%3];
        }return c;
    }

    public void shuffle(){
        Random rand = new Random();
        for (int i = 0; i < s.length; i++) {
			int swap = rand.nextInt(s.length);
			double temp = s[swap];
			s[swap] = s[i];
			s[i] = temp;
		}
    }

    public void Change(int[] pos, double new_){
        int s_pos = 0, buffer = 1;
        for(int i=2; i>=0; i--){
            try{
                if(pos[i] < l[i]) s_pos += pos[i] * buffer;
                else return;
                buffer *= l[i];
            }catch(Exception invalid){return;}
        }s[s_pos] = new_;
    }

    public void Change(int pos, double new_){
        if(pos >= 0 && pos < s.length) s[pos] = new_;
    }

    public void Resize(int[] new_size, boolean Lossless){
        Limit(new_size);
        if(productAll(new_size) == All && Lossless){
            l = new_size.clone();
            return;
        }else if(Lossless) return;
        double[] new_arr = new double[productAll(new_size)];
        for(int i = 0; i < productAll(new_size); i++){
            new_arr[i] = getNum(NumToPos(new_size, i));
        }
        s = new_arr.clone();
        l = new_size.clone();
    }

    public Array Length(){
        double[] a = new double[3];
        for(int i=0; i<3; i++){
            a[i] = (double)l[i];
        }return new Array(a);
    }

    public static class Arrays{
        public static HashMap<String, Arrays> stuff = new HashMap<>();
        public HashMap<String, Array> storage = new HashMap<>();
    }
}