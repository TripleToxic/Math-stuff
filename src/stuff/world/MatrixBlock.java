
package stuff.world;

import arc.func.Cons2;
import arc.func.Prov;
import arc.scene.event.EventListener;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.TextField;
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
        
        //JavaScript portability nightmare
        Cell<ScrollPane> paneCell;
        ScrollPane pane;
        Matrix choseMat;

        @Override
        public void buildConfiguration(Table table){
            Cons2<Boolean, MatrixBuild> updatePane = (upSize, that) -> {
                Table paneTable = (Table)pane.getWidget();
                paneTable.clearChildren();
                paneTable = that.setTable(paneTable);
                pane.setWidget(paneTable);
                if (upSize) {
                    paneCell.minWidth(Math.max(that.width, that.color * that.choseMat.column));
                }
            };

            table.background(Styles.black6);

            final int height = 500;

            CheckBox c = table.check("", v -> {
                this.edit = v;
                updatePane.get(true, this);
            }).size(40).right().pad(10).get();
            c.setChecked(this.edit);

            paneCell = table.pane(t -> t.top()).height(height).pad(10).left().colspan(4);
            pane = paneCell.get();
            pane.setOverscroll(false, false);
            pane.setSmoothScrolling(false);
            updatePane.get(true, this);
        }

        private Table setTable(Table table){
            int count = 0;
            choseMat = matTrack.get(0);

            for (int j = 0; j < choseMat.mem.length; j++){

                if (count % choseMat.column != 0) {
                    table.add(" [gray]|[] ");
                } else {
                    table.row();
                }

                table.add("[accent]#" + j).left().width(60).get().setAlignment(Align.center);

                int index = j;
                float[] t1 = {0}, t2 = {0};
                float[] lastVal = {(float)choseMat.mem[index]};
                int[] lastColor = {0}; /* [], [red], [green] */
                
                Prov<String> upVal = () -> {
                    float val = (float)choseMat.mem[index];
                    if (val != lastVal[0]) {
                        lastVal[0] = val;
                        t1[0] = Time.time + 5;
                        t2[0] = t1[0] + 15;
                        return "[red]" + val;
                    }

                    if (t1[0] >= Time.time) {
                        return null;
                    }

                    if (t2[0] >= Time.time) {
                        if (lastColor[0] == 2) {
                            return null;
                        }

                        lastColor[0] = 2;
                        return "[green]" + val;
                    }

                    if (lastColor[0] == 0) {
                        return null;
                    }

                    lastColor[0] = 0;
                    return String.valueOf(val);
                };

                float min = Math.max(this.width, this.color * choseMat.column);
                min = min / choseMat.column - 90;
                if (this.edit) {
                    @SuppressWarnings("unchecked")
                    Cell<TextField>[] cell = new Cell[1];
                    cell[0] = table.field(String.valueOf(lastVal), v -> {
                        Seq<EventListener> listens = cell[0].get().getListeners();
                        listens.remove(listens.size - 1);
                        choseMat.mem[index] = Double.parseDouble(v);
                        cell[0].tooltip(v + ", " + v.length());
                    }).width(min).right().tooltip(lastVal + ", " + String.valueOf(lastVal).length());
                }else{
                    Label lab = new Label(String.valueOf(lastVal));
                    lab.setAlignment(Align.right);
                    table.add(lab).minWidth(min).right();
                    lab.update(() -> {
                        String val = upVal.get();
                        if (val != null) {
                            lab.setText(val);
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
