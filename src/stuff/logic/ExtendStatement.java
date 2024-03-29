package stuff.logic;

import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import mindustry.logic.*;
import mindustry.ui.Styles;

public abstract class ExtendStatement extends LStatement{
    // Modified width from 144 to 70
    protected Cell<TextField> field2(Table table, Object result, Cons<String> setter){
        return table.field(result.toString(), Styles.nodeField, s -> setter.get(sanitize(s)))
            .size(70f, 40f).pad(2f).color(table.color).maxTextLength(LAssembler.maxTokenLength);
    }

    protected Cell<TextField> field3(Table table, Object result, Cons<String> setter){
        return table.field(result.toString(), Styles.nodeField, s -> setter.get(sanitize(s)))
            .size(100f, 40f).pad(2f).color(table.color).maxTextLength(LAssembler.maxTokenLength);
    }
}
