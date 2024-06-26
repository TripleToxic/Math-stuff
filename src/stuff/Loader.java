package stuff;

import mindustry.game.EventType.*;
import mindustry.mod.*;
import stuff.content.Blockss;
import stuff.dialog.MSDialog;
import stuff.logic.*;
import arc.Events;
import arc.graphics.g2d.*;

import java.util.concurrent.ForkJoinPool;

import static arc.Core.*;

public class Loader extends Mod{
    public static TextureRegion integral, leftBracket, rightBracket;
    public static final ForkJoinPool pool = ForkJoinPool.commonPool();

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
