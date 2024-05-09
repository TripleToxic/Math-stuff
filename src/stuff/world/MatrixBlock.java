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
import stuff.Loader;
import stuff.util.Matrix;

import static stuff.dialog.MSDialog.*;
import static stuff.util.AdditionalFunction.*;
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
        configurable = true;
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
        public int page = 1, maxPage = 1;

        private static final int cellWidth = 250, cellHeight = 50;
        private static final float scale = 1.3f, textScale = 0.8f;
        private boolean edit = false;
        private Matrix choseMat;

        @Override
        public void buildConfiguration(Table table){
            table.clearChildren();

            table.background(Styles.black6);

            this.setTable(table);

            table.row();

            table.add();

            table.table(t -> {
                t.defaults().size(100f, 60f);

                CheckBox c = t.check("", v -> {
                    edit = v;
                    buildConfiguration(table);
                }).size(40).right().pad(10).get();
                c.setChecked(edit);

                TextButton b1 = t.button("Delete", () -> {
                    maxPage--;
                    matTrack.remove(page - 1);
                    if(page > 1) page--;
                    buildConfiguration(table);
                }).left().get();
                b1.visible(() -> (matTrack.size > 0)  && ((page != maxPage) || (matTrack.size == matrixCap))).updateVisibility();

                ImageButton b2 = t.button(Icon.leftOpen, () -> {
                    page--;
                    buildConfiguration(table);
                }).get();
                b2.visible(() -> page > 1).updateVisibility();

                t.add("  " + page + " / " + maxPage + "  ").center();

                ImageButton b3 = t.button(Icon.rightOpen, () -> {
                    page++;
                    buildConfiguration(table);
                }).get();
                b3.visible(() -> page < maxPage).updateVisibility();

                TextButton b4 = t.button("Create", () -> {
                    createDialog.build = this;
                    createDialog.config = table;
                    createDialog.show();
                }).right().get();
                b4.visible(() -> maxPage < matrixCap).updateVisibility();
            }).growX().center();
        }

        private Table setTable(Table table){
            if(page - 1 == matTrack.size){
                return table;
            }

            choseMat = matTrack.get(page - 1);

            table.add();

            table.add("Matrix [accent]#" + page + "[white]  " + choseMat.name).center();

            table.row();

            table.table(t -> {
                t.image(Loader.leftBracket).right().get().setScale(scale * textScale * choseMat.row / 16);
            }).right().get().setScale(textScale);

            table.table(t -> {
                int count = 0;

                for (int j = 0; j < choseMat.mem.length; j++){

                    if(count % choseMat.column == 0) t.row();

                    int index = j;
                    float[] t1 = {0}, t2 = {0};
                    double[] lastVal = {choseMat.mem[index]};
                    int[] lastColor = {0}; /* [], [red], [green] */
                    
                    Prov<String> upVal = () -> {
                        double val = choseMat.mem[index];
                        if (val != lastVal[0]) {
                            lastVal[0] = val;
                            t1[0] = Time.time + 5;
                            t2[0] = t1[0] + 15;
                            return "[red]" + val;
                        }

                        if (t1[0] >= Time.time) return null;
                            

                        if (t2[0] >= Time.time) {
                            if (lastColor[0] == 2) return null;
                            
                            lastColor[0] = 2;
                            return "[green]" + val;
                        }

                        if (lastColor[0] == 0) return null;
                        

                        lastColor[0] = 0;
                        return String.valueOf(val);
                    };

                    if(edit){
                        @SuppressWarnings("unchecked")
                        Cell<TextField>[] cell = new Cell[1];
                        cell[0] = t.field(String.valueOf(lastVal[0]), v -> {
                            Seq<EventListener> listens = cell[0].get().getListeners();
                            listens.remove(listens.size - 1);
                            choseMat.mem[index] = parseDouble(v, 0d);
                            cell[0].tooltip(v + ", " + v.length());
                        }).size(cellWidth * textScale, cellHeight * textScale).right().tooltip(lastVal[0] + ", " + String.valueOf(lastVal[0]).length());
                    }else{
                        Label lab = new Label(String.valueOf(lastVal[0]));
                        lab.setFontScale(textScale);
                        lab.setScale(textScale);
                        lab.setAlignment(Align.center);
                        t.add(lab).minSize(cellWidth * textScale, cellHeight * textScale).right();
                        lab.update(() -> {
                            String val = upVal.get();
                            if (val != null) {
                                lab.setText(val);
                            }
                        });
                    }
                    count++;
                }
            }).left();

            table.table(t -> {
                t.image(Loader.rightBracket).left().get().setScale(scale * textScale * choseMat.row / 16);
            }).left().get().setScale(textScale);

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

                /*for(double v : m.mem){
                    write.d(v);
                }*/
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
                name = read.str();
                row = read.i();
                column = read.i();
                transpose = read.bool();
                if(i < matTrack.size){
                    m = new Matrix(name, row, column);
                    matTrack.add(m);
                    m.transpose = transpose;
                
                    /*size = m.row * m.column;
                    for(int j=0; j<size; j++){
                        num = read.d();
                        m.mem[j] = num;
                    }*/
                }
            }
        }
    }
}
