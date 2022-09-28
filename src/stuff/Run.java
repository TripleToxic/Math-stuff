package stuff;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import stuff.logic.*;

public class Run extends Mod {
    public Run(){
        Events.on(ClientLoadEvent.class, e -> {Time.runTask(10f, () -> {});}); 
    }

    public void loadContent(){
        Statements.load();
    }
}
