package stuff;

import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.*;
//import stuff.content.Block;
import stuff.logic.*;
import arc.Events;
import arc.graphics.g2d.*;

import static arc.Core.*;

public class Loader extends Mod {
    public static TextureRegion integral;

    public Loader(){
        Events.on(ClientLoadEvent.class, e -> {
            integral = atlas.find("math-stuff-integral");
        });
    }

    @Override
    public void loadContent(){
        Statements.load();
        //Block.load();
    }
}
