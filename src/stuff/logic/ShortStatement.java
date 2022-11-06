package stuff.logic;

import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import mindustry.logic.*;
import mindustry.ui.Styles;

public abstract class ShortStatement extends LStatement{
    // Modified width from 144 to 70
    protected Cell<TextField> field2(Table table, String result, Cons<String> setter){
        return table.field(result, Styles.nodeField, s -> setter.get(sanitize(s)))
            .size(70f, 40f).pad(2f).color(table.color).maxTextLength(LAssembler.maxTokenLength);
    }
}
