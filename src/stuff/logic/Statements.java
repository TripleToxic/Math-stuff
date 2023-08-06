package stuff.logic;

import arc.func.*;
import arc.graphics.Color;
import arc.scene.ui.layout.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.TheInstruction.*;
import stuff.util.Function;

//import stuff.logic.AFunc.TwoType;
import static stuff.util.AdditionalFunction.*;

public class Statements {
    public static class ComplexOperationStatement extends ShortStatement{
        public CFunc Op = CFunc.New;
        public String result = "result", r = "r", i = "i";
        
        public ComplexOperationStatement(String Op, String r, String i, String result){
            try{this.Op = CFunc.valueOf(Op);}catch(Exception ignored){}
            this.r = r;
            this.i = i;
            this.result = result;
        }

        public ComplexOperationStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();

            if(Op == CFunc.get){
                Button(table, table);
                table.add(" from: ");
                field(table, result, str -> result = str);
                row(table);
                field(table, r, str -> r = str);
                table.add(new StringBuffer(" = ").append(result).append(".real")).color(Color.green);
                row(table);
                field(table, i, str -> i = str);
                table.add(new StringBuffer(" = ").append(result).append(".imaginary")).color(Color.green);
                return;
            }

            field(table, result, str -> result = str);
            table.add(" = ");

            if(Op == CFunc.New){
                Button(table, table);
                field(table, r, str -> r = str);
                table.add(" + ");
                field(table, i, str -> i = str);
                table.add("i");
                return;
            }
            
            if(Op.unary) Button(table, table);
            
            field(table, r, str -> r = str);

            if(Op.binary){
                Button(table, table);
                field(table, i, str -> i = str);
            }

            if(Op.real) Button(table, table);
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> Op.symbol);
                b.clicked(() -> showSelect(b, CFunc.all, Op, o -> {
                    Op = o;
                    rebuild(parent);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        public LInstruction build(LAssembler builder){
            return new ComplexOperationI(Op, builder.var(r), builder.var(i), builder.var(result));
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
                .append(r)
                .append(" ")
                .append(i)
                .append(" ")
                .append(result)
                ;
        }
    }
    
    /*public static class ArrayOperationStatement extends ShortStatement{
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
            return new AFunction(OpA, TT, build.var(a), build.var(b), build.var(c), build.var(d), build.var(e), build.var(result), a, b, result);
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
    }*/

    public static class FunctionStatement extends ShortStatement{
        public String output = "f", input = "x", a = "a", b = "b", recur_num = "0";
        public boolean recur = false;
        public FunctionEnum op = FunctionEnum.add;

        public FunctionStatement(String[] names){
            output = names[1];
            input = names[2];

            try{
                op = FunctionEnum.valueOf(names[3]);
            }catch(Exception ignore){}

            a = names[4];
            b = names[5];

            if(names[6].equals("true")) recur = true;
            else recur = false;

            recur_num = parseDouble(names[7]) + "";  //return 0 if the argument is not a number
        }

        public FunctionStatement(){}

        @Override
        public void build(Table table){
            if(Vars.mobile) rebuildMobile(table);
            else rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            boolean unary = op.isUnary;

            field3(table, output, str -> output = str);
            table.add("(");
            field3(table, input, str -> input = str);
            table.add(")");
            table.add("=");
            if(unary){
                Button(table, table);
                field(table, a, str -> a = str);
            }else{
                field(table, a, str -> a = str);
                Button(table, table);
                field(table, b, str -> b = str);
            }

            table.row();
            repeat(4, table);
            table.add("enable: ");
            Button2(table, table);
            if(recur){
                table.row();
                repeat(4, table);
                table.add("number: ");
                field(table, recur_num, str -> recur_num = str);
            }
        }

        void rebuildMobile(Table table){
            table.clearChildren();
            boolean unary = op.isUnary;

            field2(table, output, str -> output = str);
            table.add("(");
            field2(table, input, str -> input = str);
            table.add(")");
            table.add("=");
            if(unary){
                Button(table, table);
                field2(table, a, str -> a = str);
            }else{
                field2(table, a, str -> a = str);
                Button(table, table);
                field2(table, b, str -> b = str);
            }

            table.row();
            repeat(4, table);
            table.add("enable: ");
            Button2(table, table);
            if(recur){
                table.row();
                repeat(4, table);
                table.add("number: ");
                field2(table, recur_num, str -> recur_num = str);
            }
        }

        void repeat(int n, Table table){
            for(int i=0; i<n; i++){
                table.add();
            }
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> op.symbol);
                b.clicked(() -> showSelect(b, FunctionEnum.all, op, o -> {
                    op = o;
                    build(parent);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        void Button2(Table table, Table parent){
            table.button(b -> {
                b.label(() -> recur + "");
                b.clicked(() -> {
                    recur = !recur;
                    build(parent);
                });
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler builder){
            builder.putConst(output, new Function(output, input, op, a, b, recur, recur_num, builder));
            return null;
        }
        
        @Override
        public void write(StringBuilder builder) {
            builder
            .append("Function ")
            .append(output)
            .append(" ")
            .append(input)
            .append(" ")
            .append(op.name())
            .append(" ")
            .append(a)
            .append(" ")
            .append(b)
            .append(" ")
            .append(recur)
            .append(" ")
            .append(recur_num);
        }

        @Override
        public LCategory category() {
            return LCategory.operation;
        }
    }

    public static class FunctionOperationStatement extends ShortStatement{
        public String result = "result", F = "f", x = "x";

        public FunctionOperationStatement(String result, String F, String x){
            this.result = result;
            this.F = F;
            this.x = x;
        }

        public FunctionOperationStatement(){}

        @Override
        public void build(Table table) {
            if(Vars.mobile){
                field3(table, result, str -> result = str);
                table.add(" = ");
                field3(table, F, str -> F = str);
                table.add("(");
            }else{
                field(table, result, str -> result = str);
                table.add(" = ");
                field(table, F, str -> F = str);
                table.add("(");
            }
            field3(table, x, str -> x = str);
            table.add(")");
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new FunctionOperationI(builder.var(F), builder.var(x), builder.var(result));
        }

        @Override
        public void write(StringBuilder builder) {
            builder
            .append("FunctionOp ")
            .append(result)
            .append(" ")
            .append(F)
            .append(" ")
            .append(x);
        }

        @Override
        public LCategory category() {
            return LCategory.operation;
        }
        
    }
    
    public static void load(){
        registerStatement("Complex", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4]), ComplexOperationStatement::new);
        //registerStatement("Array", args -> new ArrayOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]), ArrayOperationStatement::new);
        registerStatement("Function", args -> new FunctionStatement(args), FunctionStatement::new);
        registerStatement("FunctionOp", args -> new FunctionOperationStatement(args[1], args[2], args[3]), FunctionOperationStatement::new);
    }

    public static void registerStatement(String name, Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
