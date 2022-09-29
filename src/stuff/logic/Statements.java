package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.Operations.*;

public class Statements {
    public static class OperationsStatements extends ShortStatement{
        public Func Op = Func.add;
        public String RealOutput = "Re", ImaginaryOutput = "Im", r1 = "r1", i1 = "i1", r2 = "r2", i2 = "i2";
        
        public OperationsStatements(String Op, String r1, String i1, String r2, String i2, String RealOutput, String ImaginaryOutput){
            try{
                this.Op = Func.valueOf(Op);
            }catch(Throwable ignored){}
            this.RealOutput = RealOutput;
            this.ImaginaryOutput = ImaginaryOutput;
            this.r1 = r1;
            this.i1 = i1;
            this.r2 = r2;
            this.i2 = i2;
        }

        public OperationsStatements(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            field2(table, RealOutput, str -> RealOutput = str);
            if (Op.SingleOutputCheck){
            }else{
                field2(table, ImaginaryOutput, str -> ImaginaryOutput = str);
            }
            table.add(" = ");
            if (Op.SingleInputCheck){Button(table, table);
            }else{row(table);}
            field2(table, r1, str -> r1 = str);
            field2(table, i1, str -> i1 = str);
            if (Op.SingleInputCheck != true){
                Button(table, table);
                field2(table, r2, str -> r2 = str);
                field2(table, i2, str -> i2 = str);
            }
        }
        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Op.symbol);
                b.clicked(() -> showSelect(b, Func.all, Op, o -> {
                    Op = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        public Run Build(NewAssembler b){
            return new Function(Op, b.var(r1), b.var(i1), b.var(r2), b.var(i2), b.var(RealOutput), b.var(ImaginaryOutput));
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        public void write(StringBuilder builder){
            builder
                .append("C-Operation")
                .append(RealOutput)
                .append(" ")
                .append(ImaginaryOutput)
                .append(" ")
                .append(r1)
                .append(" ")
                .append(i1)
                .append(" ")
                .append(Op.symbol)
                .append(" ")
                .append(r2)
                .append(" ")
                .append(i2)
                ;
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return null;
        }
    }
    
    public static void load(){
        registerStatement("C-Operation", args -> new OperationsStatements(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), OperationsStatements::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
