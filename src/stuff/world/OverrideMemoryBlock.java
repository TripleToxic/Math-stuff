
package stuff.world;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.world.blocks.logic.*;
import stuff.util.Matrix;

public class OverrideMemoryBlock extends MemoryBlock{

    public OverrideMemoryBlock(String name){
        super(name);
    }

    public class OverrideMemoryBuild extends MemoryBuild{
        Seq<Matrix> matTrack = new Seq<>();

        @Override
        public void buildConfiguration(Table table) {
            
        }

        @Override
        public void write(Writes write) {
            super.write(write);
        }

        @Override
        public void read(Reads read) {
            super.read(read);
        }
    }
}
