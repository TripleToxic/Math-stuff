
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
    public int matrixCap = 4;

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
            matTrack.each((m) -> {
                int j = m.column * m.row;
                for(int i=0; i<j; i++){
                    write.d(m.mem[i]);
                }
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            int cap = read.i();
            for(int i=0; i<cap; i++){
                
            }
        }
    }
}
