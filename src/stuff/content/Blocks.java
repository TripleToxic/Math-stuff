package stuff.content;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import stuff.world.blocks.logic.DenseLogicDisplay;

import static mindustry.type.ItemStack.*;

public class Blocks {
    public static Block denseLogicDisplay;

    public static void load(){
        denseLogicDisplay = new DenseLogicDisplay("dense-logic-display"){{
            requirements(Category.logic, with(Items.lead, 700, Items.silicon, 1000, Items.metaglass, 150, Items.phaseFabric, 150));

            displaySize = 8192;
            size = 8;
        }};
    }
}
