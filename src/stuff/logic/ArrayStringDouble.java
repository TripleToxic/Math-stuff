package stuff.logic;

import arc.math.Mathf;
import arc.util.Nullable;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.*;

public class ArrayStringDouble{
    private int[] Default1 = {1};
    private double[] Default2 = {1d};
    public int[] l;
    public double[] s;
    private static int length_limit = 16, array_limit = 3;

    public ArrayStringDouble(String s){
        try{
            this.l = Limit(StringToIntArray(s.substring(s.indexOf("[") + 1, s.indexOf("]"))));
            this.s = StringToDoubleArray(s.substring(s.indexOf("{") + 1, s.indexOf("}")));
            if(this.s.length != productAll(this.l)) this.s = new double[productAll(this.l)];
        }
        catch(Throwable b){
            this.l = Default1.clone();
            this.s = Default2.clone();
        }
    }

    private static int[] StringToIntArray(String str){
        String[] s = str.split(" ", 0);
        int l = s.length;
        int[] I = new int[l];
        for(int i=0; i<l; i++){
            try{I[i] = Integer.parseInt(s[i]);}catch(Throwable a){I[i] = 1;}
        }
        return I;
    }

    private static double[] StringToDoubleArray(String str){
        String[] s = str.split(" ", 0);
        int l = s.length;
        double[] I = new double[l];
        for(int i=0; i<l; i++){
            try{I[i] = Double.parseDouble(s[i]);}catch(Throwable a){I[i] = 0;}
        }
        return I;
    }

    private static int[] NumToPos(int[] arr, int num){
        int l2 = arr.length, buffer = 1;
        int[] count_arr = new int[l2];
        for(int j=0; j<l2; j++){
            count_arr[j] = num/buffer % arr[j];
            buffer *= arr[j];
        }
        return count_arr;
    }

    public static int[] Limit(int[] i){
        int j = Mathf.clamp(i.length, 1, array_limit);
        int[] new_i = new int[j];
        for(int n=0; n<j; n++){
            if(i[n] > length_limit) new_i[n] = length_limit;
            if(i[n] < 1) new_i[n] = 1;
            else{new_i[n] = i[n];}
        }return new_i;
    }

    public int productAll(int[] i){
        int d = 1;
        for (int j : i) {
            d *= j;
        }return d;
    }

    public double getNum(int[] pos){
        int[] l2;
        if(pos.length != l.length){
            l2 = Arrays.copyOf(l, pos.length);
            Arrays.fill(l2, l.length, pos.length, 1);
        }else{l2 = l.clone();}
        int s_pos = 0, buffer = 1;
        for(int i=0; i<pos.length; i++){
            if(pos[i] < l2[i]) s_pos += pos[i] * buffer;
            else return 0;
            buffer *= l2[i];
        }return s[s_pos];
    }

    public double sumAll(double[] a){
        double sum = 0;
        for(double i : a){
            sum += i;
        }
        return sum;
    }

    public void prod(double b){
        for(int i=0; i<s.length; i++){
            s[i] *= b;
        }
    }

    public void add(ArrayStringDouble b){
        

    }

    public void shuffle(){
        Random rand = new Random();
        for (int i = 0; i < s.length; i++) {
			int randomIndexToSwap = rand.nextInt(s.length);
			double temp = s[randomIndexToSwap];
			s[randomIndexToSwap] = s[i];
			s[i] = temp;
		}
    }

    public void Change(int[] pos, double new_){
        int s_pos = 0, buffer = 1;
        for(int i=0; i<pos.length; i++) {
            try{
                if(pos[i] < l[i]) s_pos += pos[i] * buffer;
                else return;
                buffer *= l[i];
            }catch(Throwable invalid){return;}
        }s[s_pos] = new_;
    }

    public void Resize(int[] new_size, boolean Static){
        new_size = Limit(new_size).clone();
        if(productAll(new_size) == productAll(l) && Static){
            l = new_size.clone();
            return;
        }else if(Static) return;
        int[] count_arr = new int[new_size.length];
        double[] new_arr = new double[productAll(new_size)];
        for(int i=0; i<new_arr.length; i++){
            count_arr = NumToPos(new_size, i);
            new_arr[i] = getNum(count_arr);
        }
        s = new_arr.clone();
        l = new_size.clone();
    }

    public String toString(){
        StringBuilder o1 = new StringBuilder("");
        StringBuilder o2 = new StringBuilder("");
        StringBuilder final_ = new StringBuilder("[");
        int i = 0;
        
        while(i<l.length - 1){
            o1.append(l[i]).append(" ");
            i++;
        }o1.append(l[i]);
        i = 0;
        while(i<s.length - 1){
            if(s[i] == floor(s[i])){;
                o2.append((int)s[i]).append(" ");
            }else{o2.append(s[i]).append(" ");}
            i++;
        }
        if(s[i] == floor(s[i])){;
            o2.append((int)s[i]);
        }else{o2.append(s[i]);}

        return final_.append(o1).append("] {").append(o2).append("}").toString();
    }
}