package stuff.dialog;

import arc.scene.ui.layout.*;
import stuff.util.Matrix;
import stuff.world.MatrixBlock.*;

import static stuff.util.AdditionalFunction.*;

public class CreateDialog extends MSBaseDialog{
    public String row = "1", column = "1", name = "A";
    public MatrixBuild build;
    public Table config;

    public CreateDialog(){
        super("@ms-create.name");
        cont.defaults().size(100f, 60f);

        cont.add("Create Matrix").center();

        cont.row();

        cont.add("Name: ");
        cont.field(name, t -> name = t);

        cont.row();

        cont.add("Row: ");
        cont.field(row, t -> row = t);

        cont.row();

        cont.add("Column: ");
        cont.field(column, t -> column = t);

        cont.row();

        cont.button("Create", () -> {
            build.matTrack.add(new Matrix(name, parseInt(row, 1), parseInt(column, 1)));
            build.maxPage++;
            build.buildConfiguration(config);
            hide();
        }).center();
    }
}