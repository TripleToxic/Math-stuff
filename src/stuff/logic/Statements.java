package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.Operations.*;

public class Statements {
    public static class OperationsStatements extends LStatement{
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
            fields(table, RealOutput, str -> RealOutput = str);
            if (Op.SingleOutputCheck){
            }else{
                fields(table, ImaginaryOutput, str -> ImaginaryOutput = str);
            }
            if (Op.SingleInputCheck){
                Button(table, table);
                fields(table, r1, str -> r1 = str);
                table.add("+");
                fields(table, "i", i1, str -> i1 = str);
            }else{
                row(table);
                fields(table, r1, str -> r1 = str);
                table.add("+");
                fields(table, "i", i1, str -> i1 = str);
                Button(table, table);
                fields(table, r2, str -> r2 = str);
                table.add("+");
                fields(table, "i", i2, str -> i2 = str);
            }
        }
        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Op.symbol);
                b.clicked(() -> showSelect(b, Func.all, Op, o -> {
                    Op = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(4f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler b){
            return new Function(Op, b.var(r1), b.var(i1), b.var(r2), b.var(i2), b.var(RealOutput), b.var(ImaginaryOutput));
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        public void write(StringBuilder builder){
            builder
                .append("ComplexOperation")
                .append(" ")
                .append(r1)
                .append("+")
                .append(i1)
                .append("i")
                .append(" ")
                .append(Op.symbol)
                .append(" ")
                .append(r2)
                .append("+")
                .append(i2)
                .append("i")
                .append(RealOutput)
                .append(ImaginaryOutput)
                ;
        }
    }
    
    public static void load(){
        registerStatement("ComplexOperation", args -> new OperationsStatements(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), OperationsStatements::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
