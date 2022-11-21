package stuff.logic;

import mindustry.logic.LExecutor;
import static stuff.AdditionalFunction.*;

public class TheInstruction extends LExecutor{

    public static class Function implements LInstruction{
        public Func Op = Func.addC;
        public int r1, r2, i1, i2, RealOutput, ImaginaryOutput;

        public Function(Func Op, int r1, int i1, int r2, int i2, int RealOutput, int ImaginaryOutput){
            this.Op = Op;
            this.r1 = r1;
            this.r2 = r2;
            this.i1 = i1;
            this.i2 = i2;
            this.RealOutput = RealOutput;
            this.ImaginaryOutput = ImaginaryOutput;
        }

        Function(){}

        @Override
        public void run(LExecutor exec){
            if (Op.isConstant){
                exec.setnum(RealOutput, Op.Constants.get(exec.num(r1)));
            }else if (Op.SingleInputCheck){
                if (Op.SingleOutputCheck){
                    exec.setnum(RealOutput, Op.SingleOutput.get(exec.num(r1), exec.num(i1)));
                }else{
                    exec.setnum(RealOutput, Op.Func2.get(exec.num(r1), exec.num(i1)));
                    exec.setnum(ImaginaryOutput, Op.Func3.get(exec.num(r1), exec.num(i1)));
                }
            }else{
                exec.setnum(RealOutput, Op.Func4.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)));
                exec.setnum(ImaginaryOutput, Op.Func5.get(exec.num(r1), exec.num(i1), exec.num(r2), exec.num(i2)));
            }
        }
    }

    public static class VFunction implements LInstruction{
        public AFunc Opv = AFunc.addA;
        public int a, b, c, n, result;

        public VFunction(AFunc Opv, int a, int b, int c, int n, int result){
            this.Opv = Opv;
            this.a = a;
            this.b = b;
            this.c = c;
            this.n = n;
            this.result = result;
        }

        VFunction(){}

        @Override
        public void run(LExecutor exec){
            if(exec.obj(a) instanceof String astr){
                if(exec.obj(c) instanceof double[][] cArr){
                    switch(Opv){
                        case AddTo -> exec.setobj(result, addArrayToRArray(cArr, StringToArr(astr), exec.numi(n)));
                        case ChangeR -> exec.setobj(result, changeRArray(cArr, StringToArr(astr), exec.numi(n)));
                    }
                }
                switch(Opv){
                    case sum -> exec.setnum(result, sum(StringToArr(astr)));
                    case scalar -> exec.setobj(result, ArrToString(prod(StringToArr(astr), exec.num(b))));
                    case AddEle -> exec.setobj(result, ArrToString(addArray(StringToArr(astr), exec.num(b), exec.numi(n))));
                    case RemoveEle -> exec.setobj(result, ArrToString(removeArray(StringToArr(astr), exec.numi(n))));
                    case Change -> exec.setobj(result, ArrToString(changeArray(StringToArr(astr), exec.num(b), exec.numi(n))));
                    case Pick -> exec.setnum(result, pickArray(StringToArr(astr), exec.numi(n)));
                    case Shift -> exec.setobj(result, ArrToString(shiftArray(StringToArr(astr), exec.numi(n))));
                    case Shuffle -> exec.setobj(result, ArrToString(shuffleArray(StringToArr(astr))));
                }if(exec.obj(b) instanceof String bstr){
                    switch(Opv){
                        case addA -> exec.setobj(result, ArrToString(AddVector(StringToArr(astr), StringToArr(bstr))));
                        case subA -> exec.setobj(result, ArrToString(SubVector(StringToArr(astr), StringToArr(bstr))));
                        case dotProd -> exec.setnum(result, DotProd(StringToArr(astr), StringToArr(bstr)));
                        case crossProd -> exec.setobj(result, ArrToString(CrossProd(StringToArr(astr), StringToArr(bstr))));
                    }
                }
            }else if(exec.obj(b) instanceof String bstr){
                switch(Opv){
                    case scalar -> exec.setobj(result, ArrToString(prod(StringToArr(bstr), exec.num(a))));
                }
            }else if(exec.obj(a) instanceof double[][] aArr){
                switch(Opv){
                    case ChangeE -> exec.setobj(result, ChangeRArrayE(aArr, exec.num(b), exec.numi(c), exec.numi(n)));
                    case Length -> {
                    }
                }
            }else{exec.setobj(result, null);}
        }
    }
}
