package stuff.util;
import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

/**
 * A class for arrays or matrices using Memory Block type
 * All methods are assumed that the arrays are checked before performing matrix operations and cannot be used outside of TheInstruction.java
 */
public class Array{
    public MemoryBuild mem;
    public int row, column, starter;

    private static final int length_limit = 16;

    public Array(MemoryBuild mem, int row, int column, int starter){
        set(mem, row, column, starter);
    }

    public Array set(MemoryBuild mem, int row, int column, int starter){
        this.mem = mem;
        this.row = Mathf.clamp(row, 1, length_limit);
        this.column = Mathf.clamp(column, 1, length_limit);
        this.starter = starter;

        return this;
    }

    public double dotProduct(Array a, Array b){
        double accum = 0;
        for(int i = 0; i<a.column; i++){
            accum += a.mem.memory[i + a.starter] * b.mem.memory[i + b.starter];
        }
        return accum;
    }

    public void addMatrix(Array a, Array b, Array c){
        for(int i=0; i<(a.column * a.row); i++){
            c.mem.memory[i + c.starter] = a.mem.memory[i + a.starter] + b.mem.memory[i + b.starter];
        }
    }

    public void subMatrix(Array a, Array b, Array c){
        for(int i=0; i<(a.column * a.row); i++){
            c.mem.memory[i + c.starter] = a.mem.memory[i + a.starter] - b.mem.memory[i + b.starter];
        }
    }

    public void mulMatrix(Array a, Array b, Array c){
        for(int i=0; i<(a.column * a.row); i++){
            c.mem.memory[i + c.starter] = a.mem.memory[i + a.starter] - b.mem.memory[i + b.starter];
        }
    }
}