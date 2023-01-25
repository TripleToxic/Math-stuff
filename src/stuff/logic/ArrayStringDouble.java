package stuff.logic;

import arc.math.Mathf;

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

    private static int[] Limit(int[] i){
        for(int n=0; n<Mathf.clamp(i.length, 1, array_limit); n++){
            if(i[n] > length_limit) i[n] = length_limit;
        }return i;
    }

    public int productAll(int[] i){
        int d = 1;
        for (int j : i) {
            d *= j;
        }return d;
    }

    public double getNum(int[] pos){
        int s_pos = 1, buffer;
        for(int i=0; i<pos.length; i++) {
            try{
                buffer = pos[i];
                if(buffer < l[i]) s_pos *= buffer;
                else return 0;
            }catch(Throwable invalid){return 0;}
        }return s[s_pos];
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
            o2.append(s[i]).append(" ");
            i++;
        }o2.append(s[i]);

        return final_.append(o1).append("] {").append(o2).append("}").toString();
    }
}