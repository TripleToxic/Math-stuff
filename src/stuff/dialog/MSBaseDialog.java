package stuff.dialog;

import arc.Events;
import mindustry.game.EventType.*;
import mindustry.ui.dialogs.*;

public class MSBaseDialog extends BaseDialog{

    public MSBaseDialog(String title) {
        super(title);
        
        shouldPause = false;
        addCloseButton();
        shown(this::rebuild);
        onResize(this::rebuild);

        Events.on(GameOverEvent.class, e -> hide());
    }

    protected void rebuild(){}
}