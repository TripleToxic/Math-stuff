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
            try{this.Op = Func.valueOf(Op);}catch(Throwable ignored){}
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
                .append("Comp ")
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
        public AFunc OpA = AFunc.Add;
        public TwoType TT = TwoType.number;
        public String result = "result", a = "A", b = "B", c = "C", d = "d";

        public ArrayOperationStatement(String OpA, String TT, String a, String b, String c, String d, String result){
            try{this.OpA = AFunc.valueOf(OpA);}catch(Throwable h){}
            try{this.TT = TwoType.valueOf(TT);}catch(Throwable h){}
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.result = result;
        }
        
        public ArrayOperationStatement(){}

        @Override
        public void build(Table table){
            rebuild(table);
        }

        void rebuild(Table table){
            table.clearChildren();
            switch(OpA){
                case Add ->{
                    
                }               
                case ChangeInt->{
                    
                }                      
                case CrossProduct->{
                    
                }                        
                case Divide->{
                    
                }                        
                case DotProd->{
                    
                }                           
                case Get->{
                    
                }                         
                case Muliply->{
                    
                }                            
                case New->{
                    
                }         
                case ProductAll->{
                    
                }                           
                case Resize->{
                    
                }                       
                case ScalarDiv->{
                    
                }                         
                case ScalarMul->{
                    
                }                           
                case Shuffle->{
                    
                }                           
                case Subtract->{
                    
                }                        
                case SumAll->{
                    
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
            }, Styles.logict, () -> {}).size(100f, 40f).pad(2f).color(table.color);
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
            return new AFunction(OpA, TT, build.var(a), build.var(b), build.var(c), build.var(d), build.var(result));
        }

        public void write(StringBuilder builder){
            builder
                .append("Arr ")
                .append(OpA.toString())
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
                .append(result);
        }

        @Override
        public LCategory category(){
            return LCategory.operation;
        }
    }

    public static void load(){
        registerStatement("comp", args -> new ComplexOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ComplexOperationStatement::new);
        registerStatement("arr", args -> new ArrayOperationStatement(args[1], args[2], args[3], args[4], args[5], args[6], args[7]), ArrayOperationStatement::new);
    }

    public static void registerStatement(String name, arc.func.Func<String[], LStatement> func, Prov<LStatement> prov){
        LAssembler.customParsers.put(name, func);
        LogicIO.allStatements.add(prov);
    }
}
