package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.LExecutorPlus.LInstructionPlus;
import stuff.logic.Operations.*;

import static stuff.AdditionalFunction.*;
import static java.lang.Math.*;

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
                .append("Complex ")
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

        @Override
        public LInstructionPlus buildplus(LAssembler builder) {
            return null;
        }
    }
    
    public static class VectorOperationsStatement extends ShortStatement{
        LAssembler L = new LAssembler();
        public VFunc Opv = VFunc.addV;
        public String scalar = "scalar", n = "3";
        public VectorOperationsStatement(String Opv, String[] a, String[] b, String[] result, String scalar, String n){
            try{
                this.Opv = VFunc.valueOf(Opv);
            }catch(Throwable ignored){}
            this.a = a;
            this.b = b;
            this.result = result;
            this.scalar = scalar;
            this.n = n;
        }
        int Line = L.var(n);
        public String[] 
        result = AlphabetFunction(Line),
        a = Spam(Line, "0"),
        b = Spam(Line, "0");

        public VectorOperationsStatement(){}

        @Override
        public void build(Table table) {
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            table.add("N = ");
            field2(table, n, str -> n = str);
            if(Opv.scalar){
                for(int I2=0; I2<Line; I2++){
                    final int inI2 = I2;
                    row(table);
                    if(I2 == ceil((double)Line/2d)){
                        field2(table, scalar, str -> scalar = str);
                        table.add(" = ");
                    }else{table.add("   ");}
                    field2(table, a[I2], str -> a[inI2] = str);
                    if(I2 == ceil((double)Line/2d)){Button(table, table);}else{table.add(" ");}
                    field2(table, b[I2], str -> b[inI2] = str);
                }
            }else{
                for(int I=0; I<Line; I++){
                    final int inI = I;
                    row(table);
                    field2(table, result[I], str -> result[inI] = str);
                    if(I == ceil((double)Line/2d)){table.add(" = ");}else{table.add("   ");}
                    field2(table, a[I], str -> a[inI] = str);
                    if(I == ceil((double)Line/2d)){Button(table, table);}else{table.add("   ");}
                    field2(table, b[I], str -> b[inI] = str);
                }
            
            }
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Opv.symbol);
                b.clicked(() -> showSelect(b, VFunc.all, Opv, o -> {
                    Opv = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(120f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstructionPlus buildplus(LAssembler build) {
            return new VFunction(Opv, 
            GetVars(a),
            GetVars(b), 
            GetVars(result), 
            build.var(scalar),
            build.var(n)
            );
        }

        public void write(StringBuilder builder){
            builder
                .append("Vector ")
                .append(Opv.name())
                .append(" ")
                .append(n);
            if(Opv.scalar){
                builder.append(" ").append(scalar);
            }else{
                for(int u=0; u<Line; u++){
                    builder.append(" ").append(result[u]);
                }
            }
            for(int u=0; u<Line; u++){
                builder.append(" ").append(a[u]).append(" ").append(b[u]);
            }
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return null;
        }
        
    }

    public static void load(){
        registerStatement("Complex", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("Vector", args -> new VectorOperationsStatement(args[1], args, args, args, args[2], args[3]), VectorOperationsStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], ShortStatement> func, Prov<LStatement> prov) {
        LAssembler.customParsers.putAll(name, func);
        LogicIO.allStatements.add(prov);
    }
}
