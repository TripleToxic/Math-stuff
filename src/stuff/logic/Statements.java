package stuff.logic;

import arc.func.*;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.Button;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.*;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.logic.*;
import mindustry.logic.LAssembler.BVar;
import mindustry.logic.LExecutor.*;
import mindustry.ui.*;
import stuff.Loader;
import stuff.logic.TheInstruction.*;
import stuff.util.Complex;
import stuff.util.NormalFunction;
import stuff.util.Polynomial;
//import stuff.logic.AFunc.TwoType;

import static stuff.util.AdditionalFunction.*;

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
                var label = b.label(() -> Op.symbol);
                b.clicked(() -> showSelect(b, CFunc.all, Op, o -> {
                    Op = o;
                    label.get().setText(Op.symbol);
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

    public static class AllocateArrayStatement extends ExtendStatement{
        public String result = "result", starter = "0", column = "1", row = "1", mem = "cell1";

        public AllocateArrayStatement(String result, String starter, String column, String row, String mem){
            this.result = result;
            this.starter = starter;
            this.column = column;
            this.row = row;
            this.mem = mem;
        }

        @Override
        public void build(Table table) {
            table.clearChildren();

            field(table, result, str -> result = str);
            table.add(" = ");

            row(table);

            table.add("at ");
            field(table, mem, str -> mem = str);

            row(table);
            
            table.add("allocate ");
            field2(table, row, str -> row = str);
            table.add(" x ");
            field2(table, column, str -> column = str);
            table.add(" matrix");

            row(table);

            table.add(" starting at");
            field2(table, starter, str -> starter = str);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new AllocateArrayI(builder.var(mem), builder.var(row), builder.var(column), builder.var(starter), builder.var(result));
        }
        
    }

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
                var label = b.label(() -> op.symbol);
                b.clicked(() -> showSelect(b, FunctionEnum.all, op, o -> {
                    op = o;
                    label.get().setText(op.symbol);
                }));
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        void Button2(Table table, Table parent){
            table.button(b -> {
                var label = b.label(() -> recur + "");
                b.clicked(() -> {
                    recur = !recur;
                    label.get().setText(recur + "");
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
                var label = b.label(() -> reversed + "");
                b.clicked(() -> {
                    reversed = !reversed;
                    label.get().setText(reversed + "");
                });
            }, Styles.logict, () -> {}).size(64f, 40f).pad(2f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler builder) {
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
            table.clearChildren();

            if(Vars.mobile){
                field3(table, result, str -> result = str);
                table.add(" = ");
                field3(table, F, str -> F = str);
            }else{
                field(table, result, str -> result = str);
                table.add(" = ");
                field(table, F, str -> F = str);
            }

            table.add("(");
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
        public void build(Table table){
            table.clearChildren();

            repeat(2, table);
            field3(table, b, s -> b = s).padTop(2f);
            table.row();
            field3(table, result, s -> result = s);
            table.add(" = ");
            
            var image = table.image(Loader.integral).padBottom(2f).get();
            image.y = image.y - 20f;

            field3(table, F, s -> F = s);
            table.add("(x) dx");
            table.row();
            repeat(2, table);
            field3(table, a, s -> a = s).padTop(2f);
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

        public RootFindingStatement(String op, String result, String F, String guess_0, String guess_1, String maxIter){
            try{this.op = RootFindingEnum.valueOf(op);}catch(Exception ignore){}
            this.result = result;
            this.F = F;
            this.guess_0 = guess_0;
            this.guess_1 = guess_1;
            this.maxIter = maxIter;
        }

        public RootFindingStatement(){}

        @Override
        public void build(Table table){
            table.clearChildren();
            

            fieldOr(table, result, str -> result = str);
            table.add(" = ");
            table.row();


            table.add("root of ");
            repeat(2, table);
            fieldOr(table, F, str -> F = str);
            table.row();


            table.add("Starting point:");
            table.row();

            table.add();
            table.add("    x");
            table.add("0").fontScale(0.5f).padBottom(2f);
            table.add(" = ");
            fieldOr(table, guess_0, str -> guess_0 = str);
            table.row();

            table.add();
            table.add("x");
            table.add("1").fontScale(0.5f).padBottom(2f);
            table.add(" = ");
            fieldOr(table, guess_1, str -> guess_1 = str);
            table.row();


            table.add("Max iteration: ");
            table.row();
            fieldOr(table, maxIter, str -> maxIter = str);
            table.row();


            table.add("Using: ");
            table.row();
            Button(table, table);
        }

        Cell<TextField> fieldOr(Table table, String value, Cons<String> setter){
            if(Vars.mobile) return field3(table, value, setter);
            else return field(table, value, setter);
        }

        void Button(Table table, Table parent){
            table.button(b -> {
                Cell<Label> text = b.label(() -> op.name().replace("_", " ")).size(100f, 40f);
                b.image(Icon.downOpen);
                
                b.clicked(() -> showSelectTable(b, (t, hide) ->{
                    Table innerTable = 
                    new Table(i -> {
                        for(RootFindingEnum e : RootFindingEnum.values){
                            String str = e.name().replace("_", " ");
                            i.button(str, Styles.flatt, () -> {
                                op = e;
                                text.get().setText(str);
                                hide.run();
                            }).size(240f, 40f).self(c -> LCanvas.tooltip(c, e)).row();
                        }
                    });

                    Stack stack = new Stack(innerTable);
                    ButtonGroup<Button> group = new ButtonGroup<>();

                    t.button(Icon.grid, Styles.squareTogglei, () -> {
                        stack.clearChildren();
                        stack.addChild(innerTable);

                        t.parent.parent.pack();
                        t.parent.parent.invalidateHierarchy();
                    }).height(50f).growX().group(group);
                    
                    t.row();
                    t.add(stack).colspan(3).width(240f).left();
                }));
            }, Styles.logict, () -> {}).size(100f, 40f).color(table.color);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new RootFindingI(op, builder.var(result), builder.var(F), builder.var(guess_0), builder.var(guess_1), builder.var(maxIter));
        }

        @Override
        public LCategory category() {
            return categoryFunction;
        }

        @Override
        public void write(StringBuilder builder) {
            builder
            .append("root ")
            .append(op.name())
            .append(" ")
            .append(result)
            .append(" ")
            .append(F)
            .append(" ")
            .append(guess_0)
            .append(" ")
            .append(guess_1)
            .append(" ")
            .append(maxIter)
            ;
        }
        
    }
    
    public static void load(){
        registerStatement("comp", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4]), ComplexOperationStatement::new);
        //registerStatement("Array", args -> new ArrayOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]), ArrayOperationStatement::new);
        registerStatement("fn", args -> new FunctionStatement(args), FunctionStatement::new);
        registerStatement("poly", args -> new PolynomialStatement(args), PolynomialStatement::new);
        registerStatement("fnop", args -> new FunctionOperationStatement(args[1], args[2], args[3]), FunctionOperationStatement::new);
        registerStatement("int", args -> new IntegralStatement(args[1], args[2], args[3], args[4]), IntegralStatement::new);
        registerStatement("root", args -> new RootFindingStatement(args[1], args[2], args[3], args[4], args[5], args[6]), RootFindingStatement::new);
    }

    public static void registerStatement(String name, Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
