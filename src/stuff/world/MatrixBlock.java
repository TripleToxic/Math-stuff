
package stuff.world;

import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import stuff.util.Matrix;

import static mindustry.Vars.*;

public class MatrixBlock extends Block{
    public static final Stat matrixCapacity = new Stat("matrixCapacity");
    public int matrixCap = 8;

    public MatrixBlock(String name){
        super(name);
        destructible = true;
        solid = true;
        group = BlockGroup.logic;
        drawDisabled = false;
        envEnabled = Env.any;
        canOverdrive = false;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.memoryCapacity, matrixCap, StatUnit.none);
    }

    public boolean accessible(){
        return !privileged || state.rules.editor;
    }

    @Override
    public boolean canBreak(Tile tile){
        return accessible();
    }

    public class MatrixBuild extends Building{
        Seq<Matrix> matTrack = new Seq<>(false, matrixCap);

        @Override
        public void buildConfiguration(Table table){
            table.background(Styles.black6);
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.i(matrixCap);
            int u = matTrack.size;
            matTrack.each(m -> {
                int j = m.column * m.row;
                write.str(m.name);
                write.i(m.row);
                write.i(m.column);
                write.bool(m.transpose);
                for(int i=0; i<j; i++){
                    write.d(m.mem[i]);
                }
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            int cap = read.i(), row, column, size;
            Matrix m;
            String name;
            boolean transpose;
            double num;
            for(int i=0; i<cap; i++){
                if(i < matTrack.size){
                    name = read.str();
                    row = read.i();
                    column = read.i();
                    transpose = read.bool();
                    m = new Matrix(name, row, column);
                    matTrack.add(m);
                    m.transpose = transpose;

                    size = m.row * m.column;
                    for(int j=0; j<size; j++){
                        num = read.d();
                        m.mem[i] = num;
                    }
                }
            }
        }
    }
}
