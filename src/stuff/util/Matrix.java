package stuff.util;
import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

/**
 * A class for matrices or arrays using Memory Block type
 * All methods are assumed that matrices are checked before performing matrix operations and cannot be used outside of TheInstruction.java
 */
public class Matrix{
    public MemoryBuild mem;
    public int row, column, starter;

    private static final int length_limit = 16;

    public Matrix(MemoryBuild mem, int row, int column, int starter){
        set(mem, row, column, starter);
    }

    public Matrix set(MemoryBuild mem, int row, int column, int starter){
        this.mem = mem;
        this.row = Mathf.clamp(row, 1, length_limit);
        this.column = Mathf.clamp(column, 1, length_limit);
        this.starter = starter;

        return this;
    }

    public double dotProduct(Matrix a, Matrix b){
        double accum = 0;
        for(int i = 0; i<a.row; i++){
            accum += a.mem.memory[i + a.starter] * b.mem.memory[i + b.starter];
        }
        return accum;
    }

    public void addMatrix(Matrix a, Matrix b, Matrix c){
        for(int i=0; i<(a.column * a.row); i++){
            c.mem.memory[i + c.starter] = a.mem.memory[i + a.starter] + b.mem.memory[i + b.starter];
        }
    }

    public void subMatrix(Matrix a, Matrix b, Matrix c){
        for(int i=0; i<(a.column * a.row); i++){
            c.mem.memory[i + c.starter] = a.mem.memory[i + a.starter] - b.mem.memory[i + b.starter];
        }
    }

    public void mulMatrix(Matrix a, Matrix b, Matrix c){
        double[] A = a.mem.memory, B = b.mem.memory, C = c.mem.memory;
        double sum = 0;
        for(int i=0; i<a.row; i++){
            for(int j=0; j<b.column; j++){
                for(int k=0; k<a.column; k++){
                    sum += A[k * b.column + i] * B[j * b.column + k];
                }
                C[i * b.column + j] = sum;
            }
        }
    }
}