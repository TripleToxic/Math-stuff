package stuff;

import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.*;
import stuff.content.Blockss;
import stuff.dialog.MSDialog;
import stuff.logic.*;
import arc.Events;
import arc.graphics.g2d.*;

import static arc.Core.*;

public class Loader extends Mod {
    public static TextureRegion integral, leftBracket, rightBracket;

    public Loader(){
        Events.on(ClientLoadEvent.class, e -> {
            integral = atlas.find("ms-integral");
            leftBracket = atlas.find("ms-left-bracket");
            rightBracket = atlas.find("ms-right-bracket");
        });
    }

    @Override
    public void loadContent(){
        Statements.load();
        Blockss.load();
    }

    @Override
    public void init() {
        MSDialog.load();
    }
}
