package stuff.logic;

import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import mindustry.logic.*;
import mindustry.ui.Styles;
import stuff.logic.Operations.Run;

public abstract class ShortStatement extends LStatement{
    // Use Run and NewAssembler instead LInstruction and LAssembler
    public abstract Run Build(NewAssembler builder);

    // Modified width from 144 to 70
    protected Cell<TextField> field2(Table table, String value, Cons<String> setter){
        return table.field(value, Styles.nodeField, s -> setter.get(sanitize(s)))
            .size(70f, 40f).pad(2f).color(table.color).maxTextLength(LAssembler.maxTokenLength);

    }
}
