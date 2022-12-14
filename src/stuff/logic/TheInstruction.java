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
            if(Op.isRealFunction) exec.setnum(RealOutput, Op.RealFunction.get(exec.num(r1)));
            if (Op.SingleInputCheck){
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
        public LengthFunction L = LengthFunction.Column;
        public LengthGroup L2 = LengthGroup.Array;
        public int a, b, c, n, result;

        public VFunction(AFunc Opv, LengthFunction L, LengthGroup L2, int a, int b, int c, int n, int result){
            this.Opv = Opv;
            this.L = L;
            this.L2 = L2;
            this.a = a;
            this.b = b;
            this.c = c;
            this.n = n;
            this.result = result;
        }

        VFunction(){}

        @Override
        public void run(LExecutor exec){
            switch(Opv){
                case NewRArray -> {
                    int Ia = exec.numi(a), Ib = exec.numi(b);
                    Ia = Ia <= limit ? Ia : limit; 
                    Ib = Ib <= limit ? Ib : limit;
                    exec.setobj(result, NewRArray(Ia, Ib));
                }
            }
            if(exec.obj(a) instanceof String astr){
                double[] aArr = StringToArr(astr);
                if(exec.obj(c) instanceof double[][] cRArr){
                    switch(Opv){
                        case AddTo -> exec.setobj(result, LimitR(addArrayToRArray(cRArr, aArr, exec.numi(n))));
                        case ChangeR -> exec.setobj(result, LimitR(changeRArray(cRArr, aArr, exec.numi(n))));
                    }
                }
                switch(Opv){
                    case sum -> exec.setnum(result, sum(aArr));
                    case scalar -> exec.setobj(result, ArrToString(prod(aArr, exec.num(b))));
                    case AddEle -> exec.setobj(result, ArrToString(Limit(addArray(aArr, exec.num(b), exec.numi(n)))));
                    case RemoveEle -> exec.setobj(result, ArrToString(removeArray(aArr, exec.numi(n))));
                    case Change -> exec.setobj(result, ArrToString(changeArray(aArr, exec.num(b), exec.numi(n))));
                    case Pick -> exec.setnum(result, pickArray(aArr, exec.numi(n)));
                    case Shift -> exec.setobj(result, ArrToString(shiftArray(aArr, exec.numi(n))));
                    case Shuffle -> exec.setobj(result, ArrToString(shuffleArray(aArr)));
                    case Length -> {
                        switch(L){
                            case Row -> {
                                switch(L2){
                                    case Array -> exec.setnum(result, aArr.length);
                                }
                            }
                        }
                    }
                }if(exec.obj(b) instanceof String bstr){
                    double[] bArr = StringToArr(bstr);
                    switch(Opv){
                        case addA -> exec.setobj(result, ArrToString(AddVector(aArr, bArr)));
                        case subA -> exec.setobj(result, ArrToString(SubVector(aArr, bArr)));
                        case dotProd -> exec.setnum(result, DotProd(aArr, bArr));
                        case crossProd -> exec.setobj(result, ArrToString(CrossProd(aArr, bArr)));
                    }
                }
            }else if(exec.obj(b) instanceof String bstr){
                switch(Opv){
                    case scalar -> exec.setobj(result, ArrToString(prod(StringToArr(bstr), exec.num(a))));
                }
            }else if(exec.obj(a) instanceof double[][] aRArr){
                switch(Opv){
                    case ChangeE -> exec.setobj(result, LimitR(ChangeRArrayE(aRArr, exec.num(b), exec.numi(c), exec.numi(n))));
                    case Length -> {
                        switch(L){
                            case Column -> {
                                switch(L2){
                                    case Array -> exec.setnum(result, 1d);
                                    case RArray -> exec.setnum(result, aRArr.length);
                                }
                            }
                            case Row -> {
                                switch(L2){
                                    case RArray -> exec.setnum(result, aRArr[0].length);
                                }
                            }
                        }
                    }
                }
            }else{exec.setobj(result, null);}
        }
    }
}
