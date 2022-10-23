package stuff.logic;

import mindustry.logic.LAssembler;

public class LAssemblerPlus extends LAssembler{
    public int[] vars(String[] a){
        LAssembler L = new LAssembler();
        int[] K = new int[a.length];
        for(int i=0; i<a.length; i++){
            K[i] = L.var(a[i]);
        }
        return K;
    }
}
