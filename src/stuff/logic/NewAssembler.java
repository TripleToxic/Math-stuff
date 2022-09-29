package stuff.logic;

import arc.struct.Seq;
import mindustry.logic.LAssembler;
import mindustry.logic.LStatement;
import stuff.logic.Operations.Run;

public class NewAssembler extends LAssembler {
    public static NewAssembler assemble(String data, boolean privileged){
        NewAssembler asm = new NewAssembler();

        Seq<LStatement> st = read(data, privileged);

        asm.instructions = st.map(l -> l.build(asm)).filter(l -> l != null).toArray(Run.class);
        return asm;
    }
}
