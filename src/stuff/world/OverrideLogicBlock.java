
package stuff.world;

/*import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.util.Nullable;
import arc.util.Structs;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.ai.UnitCommand;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.io.TypeIO;
import mindustry.io.TypeIO.BuildingBox;
import mindustry.io.TypeIO.UnitBox;
import mindustry.logic.LAccess;
import mindustry.logic.LAssembler.BVar;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.Var;
import mindustry.world.blocks.logic.LogicBlock;
import stuff.util.Complex;

import static mindustry.Vars.*;
import static mindustry.io.TypeIO.*;

public class OverrideLogicBlock extends LogicBlock{

    public OverrideLogicBlock(String name){
        super(name);
    }

    //Read and Write use the same implementations as the super class and TypeIO with modifications because exception throwing from TypeIO
    //Use high byte numbers at writing/reading to prevent overlapping, which causes errors to occur
    public class OverrideLogicBuild extends LogicBuild{
        @Override
        public void write(Writes write){
            byte[] compressed = compress(code, links);
            write.i(compressed.length);
            write.b(compressed);

            //write only the non-constant variables
            int count = Structs.count(executor.vars, v -> (!v.constant || v == executor.vars[LExecutor.varUnit]) && !(v.isobj && v.objval == null));

            write.i(count);
            for(int i = 0; i < executor.vars.length; i++){
                Var v = executor.vars[i];

                //null is the default variable value, so waste no time serializing that
                if(v.isobj && v.objval == null) continue;

                //skip constants
                if(v.constant && i != LExecutor.varUnit) continue;

                //write the name and the object value
                write.str(v.name);

                Object value = v.isobj ? v.objval : v.numval;
                writeObject(write, value); //this one is differ from original implementation
            }

            //no memory
            write.i(0);

            if(privileged){
                write.s(Mathf.clamp(ipt, 1, maxInstructionsPerTick));
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            if(revision >= 1){
                int compl = read.i();
                byte[] bytes = new byte[compl];
                read.b(bytes);
                readCompressed(bytes, false);
            }else{
                code = read.str();
                links.clear();
                short total = read.s();
                for(int i = 0; i < total; i++){
                    read.i();
                }
            }

            int varcount = read.i();

            //variables need to be temporarily stored in an array until they can be used
            String[] names = new String[varcount];
            Object[] values = new Object[varcount];

            for(int i = 0; i < varcount; i++){
                String name = read.str();
                Object value = readObjectBoxed(read, true);

                names[i] = name;
                values[i] = value;
            }

            int memory = read.i();
            //skip memory, it isn't used anymore
            read.skip(memory * 8);

            loadBlock = () -> updateCode(code, false, asm -> {
                //load up the variables that were stored
                for(int i = 0; i < varcount; i++){
                    BVar dest = asm.getVar(names[i]);

                    if(dest != null && (!dest.constant || dest.id == LExecutor.varUnit)){
                        dest.value =
                            values[i] instanceof BuildingBox box ? box.unbox() :
                            values[i] instanceof UnitBox box ? box.unbox() :
                            values[i];
                    }
                }
            });

            if(privileged && revision >= 2){
                ipt = Mathf.clamp(read.s(), 1, maxInstructionsPerTick);
            }
        }

    }
    
    @Nullable
    public static void writeObject(Writes write, Object object){
        try{
            TypeIO.writeObject(write, object);
        }catch(IllegalArgumentException i){
            if(object instanceof Complex c){
                write.b(127);
                write.d(c.r);
                write.d(c.i);
            }else{
                throw new IllegalArgumentException("Unknown object type: " + object.getClass());
            }
        }
    }
    
    @Nullable
    public static Object readObjectBoxed(Reads read, boolean box){
        byte type = read.b();
        return switch(type){
            case 0 -> null;
            case 1 -> read.i();
            case 2 -> read.l();
            case 3 -> read.f();
            case 4 -> readString(read);
            case 5 -> content.getByID(ContentType.all[read.b()], read.s());
            case 6 -> {
                short length = read.s();
                IntSeq arr = new IntSeq(length);
                for(int i = 0; i < length; i ++) arr.add(read.i());
                yield arr;
            }
            case 7 -> new Point2(read.i(), read.i());
            case 8 -> {
                byte len = read.b();
                Point2[] out = new Point2[len];
                for(int i = 0; i < len; i ++) out[i] = Point2.unpack(read.i());
                yield out;
            }
            case 9 -> content.<UnlockableContent>getByID(ContentType.all[read.b()], read.s()).techNode;
            case 10 -> read.bool();
            case 11 -> read.d();
            case 12 -> !box ? world.build(read.i()) : new BuildingBox(read.i());
            case 13 -> LAccess.all[read.s()];
            case 14 -> {
                int blen = read.i();
                byte[] bytes = new byte[blen];
                read.b(bytes);
                yield bytes;
            }
            //unit command
            case 15 -> {
                read.b();
                yield null;
            }
            case 16 -> {
                int boollen = read.i();
                boolean[] bools = new boolean[boollen];
                for(int i = 0; i < boollen; i ++) bools[i] = read.bool();
                yield bools;
            }
            case 17 -> !box ? Groups.unit.getByID(read.i()) : new UnitBox(read.i());
            case 18 -> {
                int len = read.s();
                Vec2[] out = new Vec2[len];
                for(int i = 0; i < len; i ++) out[i] = new Vec2(read.f(), read.f());
                yield out;
            }
            case 19 -> new Vec2(read.f(), read.f());
            case 20 -> Team.all[read.ub()];
            case 21 -> readInts(read);
            case 22 -> {
                int objlen = read.i();
                Object[] objs = new Object[objlen];
                for(int i = 0; i < objlen; i++) objs[i] = readObjectBoxed(read, box);
                yield objs;
            }
            case 23 -> UnitCommand.all.get(read.us());
            case 127 -> new Complex(read.d(), read.d());
            default -> throw new IllegalArgumentException("Unknown object type: " + type);
        };
    }
}*/
