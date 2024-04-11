package stuff.content;

import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.meta.BuildVisibility;
import stuff.world.OverrideLogicBlock;

import static mindustry.type.ItemStack.*;

public class Block {
    public static void load(){
        Blocks.microProcessor = new OverrideLogicBlock("micro-processor"){{
            requirements(Category.logic, with(Items.copper, 90, Items.lead, 50, Items.silicon, 50));

            instructionsPerTick = 2;
            size = 1;
        }};

        Blocks.logicProcessor = new OverrideLogicBlock("logic-processor"){{
            requirements(Category.logic, with(Items.lead, 320, Items.silicon, 80, Items.graphite, 60, Items.thorium, 50));

            instructionsPerTick = 8;
            range = 8 * 22;
            size = 2;
        }};

        Blocks.hyperProcessor = new OverrideLogicBlock("hyper-processor"){{
            requirements(Category.logic, with(Items.lead, 450, Items.silicon, 150, Items.thorium, 75, Items.surgeAlloy, 50));

            consumeLiquid(Liquids.cryofluid, 0.08f);
            hasLiquids = true;

            instructionsPerTick = 25;
            range = 8 * 42;
            size = 3;
        }};

        Blocks.worldProcessor = new OverrideLogicBlock("world-processor"){{
            requirements(Category.logic, BuildVisibility.editorOnly, with());

            canOverdrive = false;
            targetable = false;
            instructionsPerTick = 8;
            forceDark = true;
            privileged = true;
            size = 1;
            maxInstructionsPerTick = 500;
            range = Float.MAX_VALUE;
        }};
    }
}
