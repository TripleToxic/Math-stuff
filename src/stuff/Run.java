package stuff;

import arc.*;
import arc.func.Prov;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.logic.LStatement;
import stuff.logic.*;

public class Run extends Mod {
    public Run(){
        Events.on(ClientLoadEvent.class, e -> {
            Time.runTask(10f, () -> {
            BaseDialog dialog = new BaseDialog("frog");
            dialog.cont.add("behold").row();
            dialog.cont.image(Core.atlas.find("example-java-mod-frog")).pad(20f).row();
            dialog.cont.button("I see", dialog::hide).size(100f, 50f);
            dialog.show();
            });
        }); 
    }

    public void loadContent(Prov<LStatement> OperationsStatements){
        Statements.load();
    }
    
}
