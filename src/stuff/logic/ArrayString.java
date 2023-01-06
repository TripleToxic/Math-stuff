package stuff.logic;

public class ArrayString{
    public double[] s;
    public int[] l;

    public ArrayString(String s){
        try{this.s = StringToDoubleArray(s.substring(s.indexOf("{") + 1, s.indexOf("}")));}catch(Throwable a){this.s = ZeroDoubleArray(s);}
        this.l = StringToIntArray(s.substring(s.indexOf("[") + 1, s.indexOf("]")));
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

    private static double[] ZeroDoubleArray(String str){
        String[] s = str.split(" ", 0);
        int l = s.length;
        double[] I = new double[l];
        for(int i=0; i<l; i++){
            I[i] = 0;
        }
        return I;
    }
}
