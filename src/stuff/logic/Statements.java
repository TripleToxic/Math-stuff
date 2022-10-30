package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.LExecutorPlus.*;

import static stuff.AdditionalFunction.*;

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

        @Override
        public LInstructionPlus buildplus(LAssemblerPlus builder) {
            return null;
        }
    }

    public static class SetArrStatement extends ShortStatement{
        public String T[][] = {{"result"}, AlphabetFunction(7)};
        public String[] F = Spam(7, "0");

        public SetArrStatement(String T, String[] F){
            this.T[0][0] = T;
            this.F = F;
        }

        public SetArrStatement(){}

        @Override 
        public void build(Table table){
            field(table, T[0][0], str -> T[0][0] = str);
            table.add(" = ");
            for(int i=0; i<7; i++){
                final int in = i;
                field2(table, F[i], str -> F[in] = str);
            }
        }

        public LCategory category(){
            return LCategory.operation;
        }

        @Override
        public LInstructionPlus buildplus(LAssemblerPlus b) {
            return new SetArray(b.vars(T[1]), b.vars(F));
        }

        public void write(StringBuilder builder){
            builder.append("setarr ").append(T[0][0]);
            for(int i=0; i<7; i++){
                builder.append(" ").append(F[i]);
            }
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return null;
        }
    }
    
    public static class VectorOperationsStatement extends ShortStatement{
        public VFunc Opv = VFunc.addV;
        public String scalar = "result";
        public String[][] 
        result = {{"result"}, AlphabetFunction(7)},
        a = {{"A"}, Spam(7, "0")},
        b = {{"B"}, Spam(7, "0")};

        public VectorOperationsStatement(String Opv, String a, String b, String result, String scalar){
            try{
                this.Opv = VFunc.valueOf(Opv);
            }catch(Throwable ignored){}
            this.a[0][0] = a;
            this.b[0][0] = b;
            this.result[0][0] = result;
            this.scalar = scalar;
        }
        
        public VectorOperationsStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            if(Opv.scalar){field(table, scalar, str -> scalar = str);}else{field(table, result[0][0], str -> result[0][0] = str);}
            table.add(" = ");
            field2(table, a[0][0], str -> a[0][0] = str);
            Button(table, table);
            field2(table, b[0][0], str -> b[0][0] = str);
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
        public LInstructionPlus buildplus(LAssemblerPlus build) {
            return new VFunction(Opv, build.vars(a[1]), build.vars(b[1]), build.vars(result[1]), build.var(scalar));
        }

        public void write(StringBuilder builder){
            
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
        registerStatement("comp", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("setarr", args -> new SetArrStatement(args[1], args), SetArrStatement::new);
        registerStatement("vect", args -> new VectorOperationsStatement(args[1], args[2], args[3], args[4], args[5]), VectorOperationsStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov) {
        LAssemblerPlus.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
