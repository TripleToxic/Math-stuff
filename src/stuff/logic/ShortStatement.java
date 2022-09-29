package stuff.logic;

import mindustry.logic.LStatement;
import stuff.logic.Operations.Run;

public abstract class ShortStatement extends LStatement{
    // Use Run and NewAssembler instead LInstruction and LAssembler
    public abstract Run Build(NewAssembler builder);
}
