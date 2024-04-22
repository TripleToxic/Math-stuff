package stuff.util;

import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

import static java.lang.Math.*;

/**
 * A class for matrices or arrays using Memory Block type.
 * 
 * All methods are assumed that matrices are checked before performing matrix operations.
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

    public void set(Matrix A){
        System.arraycopy(A.get(), 0, this.get(), 0, A.get().length);
    }

    public double get(int x, int y){
        return mem.memory[x + y * column];
    }

    public double[] get(){
        return mem.memory;
    }

    public void setIdentity(){
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                get()[j + i * column] = i == j ? 1.0 : 0.0;
            }
        }
    }
    
    public double dotProduct(Matrix A, Matrix B){
        double accum = 0;
        for(int i = 0; i<A.row; i++){
            accum += A.get()[i + A.starter] * B.get()[i + B.starter];
        }
        return accum;
    }

    public static void addMatrix(Matrix A, Matrix B, Matrix C){
        for(int i=0; i<A.get().length; i++){
            C.get()[i + C.starter] = A.get()[i + A.starter] + B.get()[i + B.starter];
        }
    }

    public static void subMatrix(Matrix A, Matrix B, Matrix C){
        for(int i=0; i<A.get().length; i++){
            C.get()[i + C.starter] = A.get()[i + A.starter] - B.get()[i + B.starter];
        }
    }

    public static void mulMatrix(Matrix A, Matrix B, Matrix C){
        double sum = 0;
        for(int i=0; i<A.row; i++){
            for(int j=0; j<B.column; j++){
                for(int k=0; k<A.column; k++){
                    sum += A.get(k, i) * B.get(j, k);
                }
                C.get()[j + i * A.row] = sum;
            }
        }
    }

    public static void invMatrix(Matrix A, Matrix B){
        B.set(A);
        for(int i=0; i<B.row; i++){
            
        }
    }
}