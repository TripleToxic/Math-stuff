package stuff.util;

import arc.math.Mathf;
import arc.util.pooling.Pool;

import static java.lang.Math.*;

/**
 * A class for matrices or arrays using Matrix Block type.
 * 
 * <p>All methods are assumed that matrices are checked before performing matrix operations.
 */
public class Matrix{
    public final String name;
    public final double[] mem;
    public final int row, column;
    public boolean transpose = false;

    private static final int length_limit = 16;

    public Matrix(String name, int row, int column){
        this.name = name;
        this.row = Mathf.clamp(row, 1, length_limit);
        this.column = Mathf.clamp(column, 1, length_limit);
        this.mem = new double[row * column];
    }

    public void set(Matrix A){
        System.arraycopy(A.mem, 0, mem, 0, A.mem.length);
    }

    public int getp(int x, int y){
        return x + y * column;
    }

    public double get(int x, int y){
        return mem[getp(x, y)];
    }

    public void setIdentity(){
        for(int i=0; i<row; i++){
            for(int j=0; j<column; j++){
                mem[getp(j, i)] = i == j ? 1.0 : 0.0;
            }
        }
    }
    
    public double dotProduct(Matrix A, Matrix B){
        double accum = 0;
        for(int i = 0; i<A.row; i++){
            accum += A.mem[i] * B.mem[i];
        }
        return accum;
    }

    public void addMatrix(Matrix A, Matrix B){
        for(int i=0; i<A.mem.length; i++){
            mem[i] = A.mem[i] + B.mem[i];
        }
    }

    public void subMatrix(Matrix A, Matrix B){
        for(int i=0; i<A.mem.length; i++){
            mem[i] = A.mem[i] - B.mem[i];
        }
    }

    public void mulMatrix(Matrix A, double a){
        for(int i=0; i<A.mem.length; i++){
            mem[i] = A.mem[i];
        }
    }

    public void mulMatrix(Matrix A, Matrix B){
        double sum = 0;
        for(int i=0; i<A.row; i++){
            for(int j=0; j<B.column; j++){
                for(int k=0; k<A.column; k++){
                    sum += A.get(k, j) * B.get(i, k);
                }
                mem[i + j * A.row] = sum;
            }
        }
    }

    /**
     * @Return the inverse of a matrix, or an identity matrix if the matrix A is non-invertable
     */
    public void invMatrix(Matrix A){
        int rowSelected = 0;
        double pivot;
        short inactive = 0, one = 1;
        double[] Bg = mem;

        set(A);

        for(int i=0; i<row; i++){
            pivot = 0;
            for(int j=0; j<row; j++){
                if((abs(get(j, j)) > pivot) && (((inactive >> j) & one) != one)){
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

            Bg[rowSelected + rowSelected * column] = 1;
            for(int j=0; j<column; j++){
                Bg[getp(j, rowSelected)] /= pivot;
            }

            for(int j=0; j<row; j++){
                if(j == rowSelected) continue;
                pivot = get(rowSelected, j);

                Bg[getp(rowSelected, j)] = 0;

                for(int k=0; k<column; k++){
                    Bg[getp(k, j)] -= get(k, rowSelected) * pivot;
                }
            }
        }
    }
}
