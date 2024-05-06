
package stuff.world;

import arc.func.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import stuff.util.Matrix;

import static stuff.dialog.MSDialog.*;
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
        public Seq<Matrix> matTrack = new Seq<>(false, matrixCap);

        boolean edit = false;
        public int cellWidth = 200,
            page = 1,
            maxPage = 1,
            maxColumn = 6;

        Matrix choseMat;

        @Override
        public void buildConfiguration(Table table){
            table.background(Styles.black6);
            CheckBox c = table.check("edit: ", v -> {
                edit = v;
                update(table);
            }).size(40).right().pad(10).get();

            table.row();
            c.setChecked(edit);

            update(table);
        }

        public void update(Table table){
            table.clearChildren();
            this.setTable(table);

            table.row();

            table.table(t -> {
                t.defaults().size(140f, 60f);

                TextButton b1 = table.button("Delete", () -> {
                    maxPage--;
                    matTrack.remove(page - 1);
                    if(page > 1) page--;
                    update(table);
                }).left().get();
                b1.visible(() -> (matTrack.size > 0)  && ((page != maxPage) || (matTrack.size == matrixCap))).updateVisibility();

                ImageButton b2 = table.button(Icon.leftOpen, () -> {
                    page--;
                    update(table);
                }).get();
                b2.visible(() -> page > 1).updateVisibility();

                table.add("  " + page + " / " + maxPage + "  ").center();

                ImageButton b3 = table.button(Icon.rightOpen, () -> {
                    page++;
                    update(table);
                }).get();
                b3.visible(() -> page < maxPage).updateVisibility();

                TextButton b4 = table.button("Create", () -> {
                    createDialog.build = this;
                    createDialog.config = table;
                    createDialog.show();
                }).right().get();
                b4.visible(() -> maxPage < matrixCap).updateVisibility();
            }).center();
        }

        private Table setTable(Table table){
            Log.info(table.getWidth());
            if(page - 1 == matTrack.size){
                return table;
            }

            int count = 0;
            choseMat = matTrack.get(page - 1);

            table.table(t -> {
                t.add("[").get().setFontScale(1f, (float)choseMat.row);
            });

            for (int j = 0; j < choseMat.mem.length; j++){

                if(count % choseMat.column == 0) table.row();

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

                if(edit){
                    @SuppressWarnings("unchecked")
                    Cell<TextField>[] cell = new Cell[1];
                    cell[0] = table.field(String.valueOf(lastVal[0]), v -> {
                        Seq<EventListener> listens = cell[0].get().getListeners();
                        listens.remove(listens.size - 1);
                        choseMat.mem[index] = Double.parseDouble(v);
                        cell[0].tooltip(v + ", " + v.length());
                    }).width(cellWidth).right().tooltip(lastVal[0] + ", " + String.valueOf(lastVal[0]).length());
                }else{
                    Label lab = new Label(String.valueOf(lastVal[0]));
                    lab.setAlignment(Align.right);
                    table.add(lab).minWidth(cellWidth).right();
                    lab.update(() -> {
                        String val = upVal.get();
                        if (val != null) {
                            lab.setText(val);
                        }
                    });
                }

                count++;
            }
            table.table(t -> {
                t.add("]").get().setFontScale(1f, (float)choseMat.row);
            });

            table.background(Styles.black6);
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

                for(double v : m.mem){
                    write.d(v);
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
                        m.mem[j] = num;
                    }
                }
            }
        }
    }
}
