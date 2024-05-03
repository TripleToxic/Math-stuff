
package stuff.world;

import arc.func.Prov;
import arc.scene.event.EventListener;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.Tooltip.Tooltips;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.Align;
import arc.util.Time;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import stuff.util.Matrix;

import static mindustry.Vars.*;

import java.util.List;

public class MatrixBlock extends Block{
    public static final Stat matrixCapacity = new Stat("matrixCapacity");
    public int matrixCap = 8;

    public MatrixBlock(String name){
        super(name);
        destructible = true;
        solid = true;
        group = BlockGroup.logic;
        drawDisabled = false;
        envEnabled = Env.any;
        canOverdrive = false;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.memoryCapacity, matrixCap, StatUnit.none);
    }

    public boolean accessible(){
        return !privileged || state.rules.editor;
    }

    @Override
    public boolean canBreak(Tile tile){
        return accessible();
    }

    public class MatrixBuild extends Building{
        Seq<Matrix> matTrack = new Seq<>(false, matrixCap);

        boolean edit = false;
        int color = 200, 
            width = 400;

        @Override
        public void buildConfiguration(Table table){
            table.background(Styles.black6);

            CheckBox c = table.check("", v -> {
                this.edit = v;
                //updatePane.get(true, this);
            }).size(40).right().pad(10).get();
            c.setChecked(this.edit);
        }

        private Table setTable(Table table){
            int count = 0;
            Matrix m = matTrack.get(0);


            for (int j = 0; j < m.mem.length; j++){

                if (count % m.column != 0) {
                    table.add(" [gray]|[] ");
                } else {
                    table.row();
                }

                table.add("[accent]#" + j).left().width(60).get().setAlignment(Align.center);

                int index = j;
                float lastTime1 = 0, lastTime2 = 0;
                float lastVal = (float)m.mem[index];
                int lastColor = 0; /* [], [red], [green] */
                Prov<String> upVal = () -> {
                    float val = (float)m.mem[index];
                    if (val != lastVal) {
                        lastVal = val;
                        lastTime1 = Time.time + 5;
                        lastTime2 = lastTime1 + 15;
                        return "[red]" + val;
                    }

                    if (lastTime1 >= Time.time) {
                        return null;
                    }

                    if (lastTime2 >= Time.time) {
                        if (lastColor == 2) {
                            return null;
                        }

                        lastColor = 2;
                        return "[green]" + val;
                    }

                    if (lastColor == 0) {
                        return null;
                    }

                    lastColor = 0;
                    return String.valueOf(val);
                };

                float min = Math.max(this.width, this.color * m.column);
                min = min / m.column - 90;
                if (this.edit) {
                    Cell<TextField> cell = table.field(String.valueOf(lastVal), v -> {
                        Seq<EventListener> listens = cell.get().getListeners();
                        listens.remove(listens.size - 1);
                        m.mem[index] = Double.parseDouble(v);
                        cell.tooltip(v + ", " + v.length());
                    }).width(min).right().tooltip(lastVal + ", " + String.valueOf(lastVal).length());
                }else{
                    Label lbl = new Label(String.valueOf(lastVal));
                    lbl.setAlignment(Align.right);
                    table.add(lbl).minWidth(min).right();
                    lbl.update(() -> {
                        String val = upVal.get();
                        if (val != null) {
                            lbl.setText(val);
                        }
                    });
                }

                count++;
            }
            return table;
        }

        @Override
        public void write(Writes write) {
            super.write(write);

            write.i(matTrack.size);

            matTrack.each(m -> {
                write.str(m.name);
                write.i(m.row);
                write.i(m.column);
                write.bool(m.transpose);

                int j = m.column * m.row;
                for(int i=0; i<j; i++){
                    write.d(m.mem[i]);
                }
            });
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);

            int cap = read.i(), row, column, size;
            Matrix m;
            String name;
            boolean transpose;
            double num;
            
            for(int i=0; i<cap; i++){
                if(i < matTrack.size){
                    name = read.str();
                    row = read.i();
                    column = read.i();
                    transpose = read.bool();
                    m = new Matrix(name, row, column);
                    matTrack.add(m);
                    m.transpose = transpose;

                    size = m.row * m.column;
                    for(int j=0; j<size; j++){
                        num = read.d();
                        m.mem[i] = num;
                    }
                }
            }
        }
    }
}
