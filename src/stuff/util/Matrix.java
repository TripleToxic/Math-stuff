package stuff.util;

import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

import static java.lang.Math.*;

/**
 * A class for matrices or arrays using Memory Block type.
 * 
 * <p>No overlapping between memory allocation
 * 
 * <p>All methods are assumed that matrices are checked before performing matrix operations.
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

    public int getp(int x, int y){
        return starter + x + y * column;
    }

    public double get(int x, int y){
        return mem.memory[getp(x, y)];
    }

    public double[] get(){
        return mem.memory;
    }

    public void setIdentity(){
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                get()[getp(j, i)] = i == j ? 1.0 : 0.0;
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

    public static void mulMatrix(Matrix A, double a, Matrix B){
        for(int i=0; i<A.get().length; i++){
            B.get()[i + B.starter] = A.get()[i + A.starter];
        }
    }

    public static void mulMatrix(Matrix A, Matrix B, Matrix C){
        double sum = 0;
        for(int i=0; i<A.row; i++){
            for(int j=0; j<B.column; j++){
                for(int k=0; k<A.column; k++){
                    sum += A.get(k, j) * B.get(i, k);
                }
                C.get()[i + j * A.row + C.starter] = sum;
            }
        }
    }

    /**
     * @Return the inverse of a matrix, or an identity matrix if the matrix A is non-invertable
     */
    public  void invMatrix(Matrix A){
        int rowSelected = 0;
        double pivot;
        short inactive = 0, one = 1;
        double[] B = get();

        set(A);

        for(int i=0; i<row; i++){
            pivot = 0;
            for(int j=0; j<row; j++){
                if((abs(get(j, j)) > pivot) && (((inactive >> j) & one) == one)){
                    pivot = abs(get(j, j));
                    rowSelected = j;
                }
            }
            
            if(pivot == 0d){
                setIdentity();
                return;
            }

            pivot = get(rowSelected, rowSelected);
            inactive |= one << rowSelected;

            B[rowSelected + rowSelected * column] = 1;
            for(int j=0; j<column; j++){
                B[getp(j, rowSelected)] /= pivot;
            }

            for(int j=0; j<row; j++){
                if(j == rowSelected) continue;
                pivot = get(rowSelected, j);

                B[getp(rowSelected, j)] = 0;

                for(int k=0; k<column; k++){
                    B[getp(k, j)] -= get(k, rowSelected) * pivot;
                }
            }
        }
    }
}