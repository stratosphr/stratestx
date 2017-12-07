package solvers.z3;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import langs.maths.def.DefsRegister;
import langs.maths.generic.bool.ABoolExpr;
import visitors.SMTEncoder;

import static com.microsoft.z3.Status.SATISFIABLE;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:12
 */
public final class Z3 {

    private static Context context = new Context();
    private static Solver solver = context.mkSolver();

    private Z3() {
    }

    public static Z3Result checkSAT(ABoolExpr expr, DefsRegister defsRegister) {
        solver.reset();
        BoolExpr accept = expr.accept(new SMTEncoder(context, solver, defsRegister));
        solver.add(accept);
        Status status = solver.check();
        return new Z3Result(expr, status, status == SATISFIABLE ? solver.getModel() : null, context);
    }

}
