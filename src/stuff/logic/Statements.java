package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.TheInstruction.*;

public class Statements {
    public static class ComplexOperationStatement extends ShortStatement{
        public Func Op = Func.addC;
        public String RealOutput = "Re", ImaginaryOutput = "Im", r1 = "r1", i1 = "i1", r2 = "r2", i2 = "i2";
        
        public ComplexOperationStatement(String Op, String r1, String i1, String r2, String i2, String RealOutput, String ImaginaryOutput){
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

        public ComplexOperationStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            field2(table, RealOutput, str -> RealOutput = str);
            if(Op.isConstant){
                table.add(" = ");
                Button(table, table);
            }else{ 
                if (Op.SingleOutputCheck){}else{field2(table, ImaginaryOutput, str -> ImaginaryOutput = str);}
                table.add(" = ");
                if (Op.SingleInputCheck){Button(table, table);}else{row(table);}
                field2(table, r1, str -> r1 = str);
                field2(table, i1, str -> i1 = str);
                if (Op.SingleInputCheck != true){
                    Button(table, table);
                    field2(table, r2, str -> r2 = str);
                    field2(table, i2, str -> i2 = str);
                }
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

        public LInstruction build(LAssembler b){
            return new Function(Op, b.var(r1), b.var(i1), b.var(r2), b.var(i2), b.var(RealOutput), b.var(ImaginaryOutput));
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        public void write(StringBuilder builder){
            builder
                .append("comp ")
                .append(Op.name())
                .append(" ")
                .append(r1)
                .append(" ")
                .append(i1)
                .append(" ")
                .append(r2)
                .append(" ")
                .append(i2)
                .append(" ")
                .append(RealOutput)
                .append(" ")
                .append(ImaginaryOutput)
                ;
        }
    }
    
    public static class VectorOperationsStatement extends ShortStatement{
        public VFunc Opv = VFunc.addV;
        public String result = "result", a = "A", b = "B";

        public VectorOperationsStatement(String Opv, String a, String b, String result){
            try{
                this.Opv = VFunc.valueOf(Opv);
            }catch(Throwable ignored){}
            this.a = a;
            this.b = b;
            this.result = result;
        }
        
        public VectorOperationsStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            field3(table, result, str -> result = str);
            table.add(" = ");
            field2(table, a, str -> a = str);
            Button(table, table);
            field2(table, b, str -> b = str);
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Opv.symbol);
                b.clicked(() -> showSelect(b, VFunc.all, Opv, o -> {
                    Opv = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(90f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler build) {
            return new VFunction(Opv, build.var(a), build.var(b), build.var(result));
        }

        public void write(StringBuilder builder){
            builder
                .append("vect ")
                .append(Opv.name())
                .append(" ")
                .append(a)
                .append(" ")
                .append(b)
                .append(" ")
                .append(result);
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }       
    }

    public static void load(){
        registerStatement("comp", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("vect", args -> new VectorOperationsStatement(args[1], args[2], args[3], args[4]), VectorOperationsStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssemblerPlus.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
