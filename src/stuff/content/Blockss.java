package stuff.content;

import mindustry.type.Category;
import mindustry.world.Block;
import stuff.world.MatrixBlock;

import static mindustry.type.ItemStack.*;

public class Blockss{
    public static Block 
    
    matrixCell, matrixBank;


    public static void load(){

        matrixCell = new MatrixBlock("matrix-cell"){{
            requirements(Category.logic, empty);

            matrixCap = 8;
            size = 1;
        }};

        matrixBank = new MatrixBlock("matrix-bank"){{
            requirements(Category.logic, empty);

            matrixCap = 16;
            size = 2;
        }};
    }
}
