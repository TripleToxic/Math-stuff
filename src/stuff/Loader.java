package stuff;

import mindustry.mod.*;
import stuff.logic.*;

import static arc.Core.*;

import arc.graphics.g2d.TextureRegion;

public class Loader extends Mod {
    public static TextureRegion integral;

    @Override
    public void loadContent(){
        integral.set(atlas.find("math-stuff-integral"));
        Statements.load();
    }
}
