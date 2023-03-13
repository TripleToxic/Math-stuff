package stuff;

import mindustry.mod.*;
import stuff.content.Blocks;
import stuff.logic.*;

public class Run extends Mod {
    public void loadContent(){
        Statements.load();
        Blocks.load();
    }
}
