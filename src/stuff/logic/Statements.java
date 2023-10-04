package stuff.logic;

import arc.func.*;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.Image;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.logic.*;
import mindustry.logic.LAssembler.BVar;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.logic.TheInstruction.*;
import stuff.util.Complex;
import stuff.util.NormalFunction;
import stuff.util.Polynomial;
//import stuff.logic.AFunc.TwoType;

import static stuff.util.AdditionalFunction.*;

import java.util.Arrays;

public class Statements{
    public static Color functionGreen = Color.valueOf("1fa32c");
    public static final LCategory categoryFunction = new LCategory("funcion", Pal.heal, Icon.settingsSmall);
    
    public static class ComplexOperationStatement extends ExtendStatement{
        public CFunc Op = CFunc.set;
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
                field(table, result, str -> {
                    if(!str.equals("")){
                        result = str;
                        rebuild(table);
                    }else result = str;
                });
                row(table);
                field(table, r, str -> r = str);
                table.add(new StringBuffer(" = ").append(result).append(".real")).color(Color.red);
                row(table);
                field(table, i, str -> i = str);
                table.add(new StringBuffer(" = ").append(result).append(".imaginary")).color(Color.red);
                return;
            }

            field(table, result, str -> result = str);
            table.add(" = ");

            if(Op == CFunc.set){
                Button(table, table);
                field3(table, r, str -> r = str);
                table.add(" + ");
                field3(table, i, str -> i = str);
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
            int R, I;
            if(Op == CFunc.set || Op == CFunc.get){
                R = builder.var(r);
                I = builder.var(i);
            }else{
                R = putComplConst(builder, r);
                I = putComplConst(builder, i);
            }
            return new ComplexOperationI(Op, R, I, putComplConst(builder, result));
        }

        public static int putComplConst(LAssembler builder, String name){
            double check = parseDouble(name);
            if(check == invalidNum){
                BVar var = builder.putConst(name, new Complex());
                builder.var(name.concat(".re"));
                builder.var(name.concat(".im"));
                return var.id;
            }else{
                return builder.putConst("___" + check, check).id;
            }
        }

        /** 
         * Reimplement everything because they are inacessible.
         * @see mindustry.logic.LAssembler
        */

        static final int invalidNum = Integer.MIN_VALUE;

        static double parseDouble(String symbol){
            if(symbol.startsWith("0b")) return Strings.parseLong(symbol, 2, 2, symbol.length(), invalidNum);
            if(symbol.startsWith("0x")) return Strings.parseLong(symbol, 16, 2, symbol.length(), invalidNum);
            if(symbol.startsWith("%") && (symbol.length() == 7 || symbol.length() == 9)) return parseColor(symbol);

            return Strings.parseDouble(symbol, invalidNum);
        }

        static double parseColor(String symbol){
            int
            r = Strings.parseInt(symbol, 16, 0, 1, 3),
            g = Strings.parseInt(symbol, 16, 0, 3, 5),
            b = Strings.parseInt(symbol, 16, 0, 5, 7),
            a = symbol.length() == 9 ? Strings.parseInt(symbol, 16, 0, 7, 9) : 255;

            return Color.toDoubleBits(r, g, b, a);
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
                .append(r)
                .append(" ")
                .append(i)
                .append(" ")
                .append(result)
                ;
        }
    }

    /*public static class ArrayOperationStatement extends ExtendStatement{
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

    public static class FunctionStatement extends ExtendStatement{
        public String output = "f", a = "a", b = "b", recur_string = "0";
        public boolean recur = false;
        public FunctionEnum op = FunctionEnum.add;

        public FunctionStatement(String[] names){
            output = names[1];

            try{
                op = FunctionEnum.valueOf(names[2]);
            }catch(Exception ignore){}

            a = names[3];
            b = names[4];

            if(names[5].equals("true")) recur = true;
            else recur = false;

            recur_string = names[6];
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
            table.add("(x) =");
            if(unary){
                Button(table, table);
                field(table, a, str -> a = str);
            }else{
                field(table, a, str -> a = str);
                Button(table, table);
                field(table, b, str -> b = str);
            }

            table.row();
            repeat(1, table);
            table.add("enable: ");
            Button2(table, table);
            if(recur){
                table.row();
                repeat(1, table);
                table.add("number: ");
                field(table, recur_string, str -> recur_string = str);
            }
        }

        void rebuildMobile(Table table){
            table.clearChildren();
            boolean unary = op.isUnary;

            field2(table, output, str -> output = str);
            table.add("(x) =");
            if(unary){
                Button(table, table);
                field2(table, a, str -> a = str);
            }else{
                field2(table, a, str -> a = str);
                Button(table, table);
                field2(table, b, str -> b = str);
            }

            table.row();
            repeat(1, table);
            table.add("enable: ");
            Button2(table, table);
            if(recur){
                table.row();
                repeat(1, table);
                table.add("number: ");
                field2(table, recur_string, str -> recur_string = str);
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
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color).tooltip("enable or disable a starting number if any part of a function call itself");
        }

        @Override
        public LInstruction build(LAssembler builder){
            builder.putConst(output, new NormalFunction(output, op, a, b, recur, recur_string, builder));
            return null;
        }
        
        @Override
        public void write(StringBuilder builder) {
            builder
            .append("fn ")
            .append(output)
            .append(" ")
            .append(op.name())
            .append(" ")
            .append(a)
            .append(" ")
            .append(b)
            .append(" ")
            .append(recur)
            .append(" ")
            .append(recur_string);
        }

        @Override
        public LCategory category() {
            return categoryFunction;
        }
    }

    static void repeat(int n, Table table){
        for(int i=0; i<n; i++){
            table.add();
        }
    }

    public static class PolynomialStatement extends ExtendStatement{
        static byte starter_byte = 0x61;
        public int degree = 2;
        public String functionName = "f";
        public String[] coefficents = init(11);
        public boolean reversed = false;

        public PolynomialStatement(String[] names){
            functionName = names[1];

            reversed = names[2].equals("true") ? true : false;

            degree = Mathf.clamp(parseInt(names[3]), 0, 11);

            System.arraycopy(names, 4, coefficents, 0, degree + 1);
            Log.info(coefficents + "\n");
        }

        public PolynomialStatement(){}

        static String[] init(int degree){
            String[] coefficents = new String[degree + 1];
            byte[] bytes = {starter_byte};
            for(int i=0; i<=degree; i++){
                coefficents[i] = new String(bytes);
                bytes[0] += 1;
            }
            return coefficents;
        }

        @Override
        public void build(Table table) {
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();

            Log.info(Arrays.toString(coefficents));
            Log.info(degree);
            table.add("degree = ");
            field2(table, degree, str -> {
                int a = Mathf.clamp(parseInt(str), 0, 11);
                if(!str.equals("")){
                    degree = a;
                    rebuild(table);
                }
                else degree = a;
            });
            table.row();

            field3(table, functionName, s -> functionName = s);
            table.add("(x) = ");
            int[] ib = {0};
            
            if(reversed){
                for(int i=degree; i>1; i--){
                    ib[0] = i;
                    row(table);
                    field2(table, coefficents[ib[0]], s -> coefficents[ib[0]] = s);
                    table.add("x");
                    table.add(i + "").fontScale(0.5f).padTop(2f);
                    table.add(" + ");
                }
                if(degree > 0){
                    field2(table, coefficents[1], s -> coefficents[1] = s);
                    table.add("x");
                    table.add(" + ");
                }
                field2(table, coefficents[0], s -> coefficents[0] = s);
            }else{
                field2(table, coefficents[0], s -> coefficents[0] = s);
                if(degree > 0){
                    table.add(" + ");
                    field2(table, coefficents[1], s -> coefficents[1] = s);
                    table.add("x");
                }
                for(int i=2; i<=degree; i++){
                    ib[0] = i;
                    table.add(" + ");
                    field2(table, coefficents[i], s -> coefficents[ib[0]] = s);
                    table.add("x");
                    table.add(i + "").fontScale(0.5f).padTop(2f);
                    row(table);
                }
            }

            table.row();
            Button(table, table);
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                b.label(() -> reversed + "");
                b.clicked(() -> {
                    reversed = !reversed;
                    build(parent);
                });
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            Log.info(Arrays.toString(coefficents));
            builder.putConst(functionName, new Polynomial(coefficents, degree, builder));
            return null;
        }

        @Override
        public LCategory category(){
            return categoryFunction;
        }

        @Override
        public void write(StringBuilder builder) {
            builder
            .append("poly ")
            .append(functionName)
            .append(" ")
            .append(reversed)
            .append(" ")
            .append(degree)
            .append(" ")
            .append(ToString(coefficents, degree + 1));
        }
    }

    public static class FunctionOperationStatement extends ExtendStatement{
        public String result = "result", F = "f", x = "x";

        public FunctionOperationStatement(String result, String F, String x){
            this.result = result;
            this.F = F;
            this.x = x;
        }

        public FunctionOperationStatement(){}

        @Override
        public void build(Table table){
            table.row();

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
            .append("fnop ")
            .append(result)
            .append(" ")
            .append(F)
            .append(" ")
            .append(x);
        }

        @Override
        public LCategory category() {
            return categoryFunction;
        }
    }

    public static class IntegralStatement extends ExtendStatement{
        public String result = "result", F = "f", a = "a", b = "b";

        public IntegralStatement(String result, String F, String a, String b){
            this.result = result;
            this.F = F;
            this.a = a;
            this.b = b;
        }

        public IntegralStatement(){}

        @Override
        public void build(Table table) {
            repeat(2, table);
            fieldsmall(table, b, s -> b = s);
            table.row();
            field3(table, result, s -> result = s);
            table.add(" = ");
            table.add("∫").fontScale(5f);
            fieldsmall(table, F, s -> F = s);
            table.add("(x) dx");
            table.row();
            repeat(2, table);
            fieldsmall(table, a, s -> a = s).padTop(2f);
        }

        Cell<TextField> fieldsmall(Table table, Object result, Cons<String> setter){
            return table.field(result.toString(), Styles.nodeField, s -> setter.get(sanitize(s)))
                .size(50f, 40f).color(table.color).maxTextLength(LAssembler.maxTokenLength);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new IntegralI(builder.var(result), builder.var(F), builder.var(a), builder.var(b));
        }
        
        @Override
        public void write(StringBuilder builder){
            builder
            .append("int ")
            .append(result)
            .append(" ")
            .append(F)
            .append(" ")
            .append(a)
            .append(" ")
            .append(b);
        }

        @Override
        public LCategory category(){
            return categoryFunction;
        }
    }

    public static class RootFindingStatement extends ExtendStatement{
        public String result = "result", F = "f", maxIter = "3", guess_0 = "-1", guess_1 = "1";
        public RootFindingEnum op = RootFindingEnum.Bisection;

        public RootFindingStatement(String op, String result, String F, String maxIter, String guess_0, String guess_1){
            try{this.op = RootFindingEnum.valueOf(op);}catch(Exception ignore){}
            this.result = result;
            this.F = F;
            this.maxIter = maxIter;
            this.guess_0 = guess_0;
            this.guess_1 = guess_1;
        }

        @Override
        public void build(Table table){
            
        }

        void Button(Table table, Table parent){
            boolean[] shown = {false};

            table.button(op.name(), Icon.downOpen, Styles.togglet, () -> {
                shown[0] = !shown[0];
            }).marginLeft(14f).width(260f).height(55f).update(t -> {
                ((Image)t.getChildren().get(1)).setDrawable(shown[0] ? Icon.upOpen : Icon.downOpen);
                t.setChecked(shown[0]);
            }).row();

            table.collapser(b -> {
                for(RootFindingEnum e : RootFindingEnum.values()){
                    
                }
                
            } ,() -> shown[0]);

        }

        @Override
        public LInstruction build(LAssembler builder) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public LCategory category() {
            // TODO Auto-generated method stub
            return super.category();
        }

        @Override
        public void write(StringBuilder builder) {
            // TODO Auto-generated method stub
            super.write(builder);
        }
        
    }
    
    public static void load(){
        registerStatement("comp", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4]), ComplexOperationStatement::new);
        //registerStatement("Array", args -> new ArrayOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]), ArrayOperationStatement::new);
        registerStatement("fn", args -> new FunctionStatement(args), FunctionStatement::new);
        registerStatement("poly", args -> new PolynomialStatement(args), PolynomialStatement::new);
        registerStatement("fnop", args -> new FunctionOperationStatement(args[1], args[2], args[3]), FunctionOperationStatement::new);
        registerStatement("int", args -> new IntegralStatement(args[1], args[2], args[3], args[4]), IntegralStatement::new);
    }

    public static void registerStatement(String name, Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
