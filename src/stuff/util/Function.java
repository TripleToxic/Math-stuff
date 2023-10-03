package stuff.util;

import mindustry.logic.LExecutor;

public interface Function {
    double evaluate(LExecutor exec, double val);

    //Constants for 7-points Gaussian quadrature. x1 = 0
    static double
    x2 = 0.405845151377397167d,
    x3 = -0.405845151377397167d,
    x4 = 0.74153118559939444d,
    x5 = -0.74153118559939444d,
    x6 = 0.949107912342758525d,
    x7 = -0.949107912342758525,

    w1 = 0.417959183673469388d,
    w23 = 0.38183005050511894d,
    w45 = 0.279705391489276668d,
    w67 = 0.129484966168869693d;

    double integral(LExecutor exec, double a, double b);
}
