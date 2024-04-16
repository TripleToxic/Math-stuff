package stuff.util;
import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

/**
 * A class for matrices or arrays using Memory Block type.
 * 
 * All methods are assumed that matrices are checked before performing matrix operations and cannot be used outside of TheInstruction.java.
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

    public double get(int x, int y){
        return mem.memory[x + y * column];
    }

    public double[] get(){
        return mem.memory;
    }

    public double dotProduct(Matrix a, Matrix b){
        double accum = 0;
        for(int i = 0; i<a.row; i++){
            accum += a.get()[i + a.starter] * b.get()[i + b.starter];
        }
        return accum;
    }

    public static void addMatrix(Matrix a, Matrix b, Matrix c){
        for(int i=0; i<(a.column * a.row); i++){
            c.get()[i + c.starter] = a.get()[i + a.starter] + b.get()[i + b.starter];
        }
    }

    public static void subMatrix(Matrix a, Matrix b, Matrix c){
        for(int i=0; i<(a.column * a.row); i++){
            c.get()[i + c.starter] = a.get()[i + a.starter] - b.get()[i + b.starter];
        }
    }

    public static void mulMatrix(Matrix a, Matrix b, Matrix c){
        double sum = 0;
        for(int i=0; i<a.row; i++){
            for(int j=0; j<b.column; j++){
                for(int k=0; k<a.column; k++){
                    sum += a.get(k, i) * b.get(j, k);
                }
                c.get()[j + i * a.row] = sum;
            }
        }
    }

    /*public void transposeMatrix(Matrix a){
        double buffer = 0;
        for(int i=0; i<a.row; i++){
            for(int j=0; j<a.column; j++){
                buffer = a.get(i, j);
                a.get()[i + j * a.row] = a.get(j, i);
                a.get()[j + i * a.row] = buffer;
            }
        }

        int buffer_int = a.row;
        a.row = a.column;
        a.column = buffer_int;

    }*/
}