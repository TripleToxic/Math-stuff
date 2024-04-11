package stuff.util;
import arc.math.Mathf;
import mindustry.world.blocks.logic.MemoryBlock.MemoryBuild;

public class Array{
    public MemoryBuild mem;
    public int row, column, starter;

    private static final int length_limit = 16;

    public Array(MemoryBuild mem, int row, int column, int starter){
        set(mem, row, column, starter);
    }

    public Array set(MemoryBuild mem, int row, int column, int starter){
        this.mem = mem;
        this.row = Mathf.clamp(row, 1, length_limit);
        this.column = Mathf.clamp(column, 1, length_limit);
        this.starter = starter;

        return this;
    }
}