package stuff.logic;

import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.TheInstruction.*;

import stuff.logic.AFunc.TwoType;

public class Statements {
    public static class ComplexOperationStatement extends ShortStatement{
        public Func Op = Func.addC;
        public String RealOutput = "Re", ImaginaryOutput = "Im", r1 = "r1", i1 = "i1", r2 = "r2", i2 = "i2";
        
        public ComplexOperationStatement(String Op, String r1, String i1, String r2, String i2, String RealOutput, String ImaginaryOutput){
            try{this.Op = Func.valueOf(Op);}catch(Exception ignored){}
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
            if (!Op.SingleOutputCheck){field2(table, ImaginaryOutput, str -> ImaginaryOutput = str);}
            table.add(" = ");
            if(Op.isRealFunction){field2(table, r1, str -> r1 = str); Button(table, table);}
            else{
                if (Op.SingleInputCheck){Button(table, table);}else{row(table);}
                field2(table, r1, str -> r1 = str);
                field2(table, i1, str -> i1 = str);
                if (!Op.SingleInputCheck){
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
                .append(Op.toString())
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
    
    public static class ArrayOperationStatement extends ShortStatement{
        public AFunc OpA = AFunc.New;
        public TwoType TT = TwoType.number;
        public String result = "result", a = "a", b = "b", c = "c", d = "d", e = "e";

        public ArrayOperationStatement(String OpA, String TT, String a, String b, String c, String d, String e, String result){
            try{this.OpA = AFunc.valueOf(OpA);}catch(Exception h){}
            try{this.TT = TwoType.valueOf(TT);}catch(Exception h){}
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.result = result;
        }
        
        public ArrayOperationStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            if(!OpA.local) {
                field3(table, result, str -> result = str);
                table.add(" = ");
            }
            switch(OpA){
                case Add ->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }               
                case Change->{
                    Button(table, table);
                    field2(table, a, str -> a = str);
                    row(table);
                    table.add(" at ");
                    Button2(table, table);
                    switch(TT){
                        case array -> {
                            row(table);
                            table.add("L:"); field2(table, b, str -> b = str);
                            row(table);
                            table.add("R:"); field2(table, c, str -> c = str);
                            row(table);
                            table.add("C:"); field2(table, d, str -> d = str);
                            row(table);
                            table.add("to ");
                            field2(table, e, str -> e = str);
                            break;
                        }
                        case number -> {
                            field2(table, b, str -> b = str);
                            table.add("to ");
                            field2(table, c, str -> c = str);
                        }
                    }
                    break;
                }                      
                case CrossProduct->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }                        
                case Divide->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    row(table);
                    table.add(" by ");
                    Button2(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }                        
                case DotProd->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }                           
                case Get->{
                    Button(table, table);
                    row(table);
                    table.add(" from ");
                    field2(table, a, str -> a = str);
                    row(table);
                    table.add(" at ");
                    Button2(table, table);
                    switch(TT){
                        case array -> {
                            row(table);
                            table.add("L:"); field2(table, b, str -> b = str);
                            row(table);
                            table.add("R:"); field2(table, c, str -> c = str);
                            row(table);
                            table.add("C:"); field2(table, d, str -> d = str);
                        }
                        case number -> {
                            field2(table, b, str -> b = str);
                        }
                    }
                    break;
                }                         
                case Muliply->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    row(table);
                    table.add(" by ");
                    Button2(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }                            
                case New->{
                    Button(table, table);
                    row(table);
                    table.add(" New array of: ");
                    row(table);
                    table.add("L:"); field2(table, a, str -> a = str);
                    row(table);
                    table.add("R:"); field2(table, b, str -> b = str);
                    row(table);
                    table.add("C:"); field2(table, c, str -> c = str);
                    break;
                }         
                case ProductAll->{
                    Button(table, table);
                    field2(table, a, str -> a = str);
                    break;
                }                           
                case Resize->{
                    Button(table, table);
                    field2(table, a, str -> a = str);
                    row(table);
                    table.add(" is lossless: ");
                    field2(table, e, str -> e = str);
                    row(table);
                    table.add(" to: ");
                    row(table);
                    table.add("L:"); field2(table, b, str -> b = str);
                    row(table);
                    table.add("R:"); field2(table, c, str -> c = str);
                    row(table);
                    table.add("C:"); field2(table, d, str -> d = str);
                    break;
                }                   
                case Shuffle->{
                    Button(table, table);
                    field2(table, a, str -> a = str);
                    break;
                }                           
                case Subtract->{
                    field2(table, a, str -> a = str);
                    Button(table, table);
                    field2(table, b, str -> b = str);
                    break;
                }                        
                case SumAll->{
                    Button(table, table);
                    field2(table, a, str -> a = str);
                    break;
                }
                case Length ->{
                    Button(table, table);
                    row(table);
                    table.add(" of ");
                    field2(table, a, str -> a = str);
                    row(table);
                    table.add(" as ");
                    Button2(table, table);
                }         
                case Assign -> {
                    field3(table, result, str -> result = str);
                    Button(table, table);
                    field(table, a, str -> a = str);
                }
            }
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> OpA.symbol);
                b.clicked(() -> showSelect(b, AFunc.all, OpA, o -> {
                    OpA = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(80f, 40f).pad(2f).color(table.color);
        }

        void Button2(Table table, Table parent){
            table.button(b -> {
                b.label(() -> TT.name());
                b.clicked(() -> showSelect(b, TwoType.all, TT, o -> {
                    TT = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(100f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler build) {
            return new AFunction(OpA, TT, build.var(a), build.var(b), build.var(c), build.var(d), build.var(e), build.var(result), a, b, result, build);
        }

        public void write(StringBuilder builder){
            builder
                .append("Array ")
                .append(OpA.name())
                .append(" ")
                .append(TT.name())
                .append(" ")
                .append(a)
                .append(" ")
                .append(b)
                .append(" ")
                .append(c)
                .append(" ")
                .append(d)
                .append(" ")
                .append(e)
                .append(" ")
                .append(result);
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }
    }

    public static void load(){
        registerStatement("Complex", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("Array", args -> new ArrayOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]), ArrayOperationStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
