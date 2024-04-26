
package stuff.world;

import arc.struct.Seq;
import mindustry.world.blocks.logic.LogicBlock;
import stuff.util.Matrix;

public class OverrideLogicBlock extends LogicBlock{

    public OverrideLogicBlock(String name){
        super(name);
    }

    public class OverrideLogicBuild extends LogicBuild{
        Seq<Matrix> matTrack = new Seq<>();
    }
}
