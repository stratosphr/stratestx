package solvers.z3;

import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import langs.maths.def.DefsRegister;
import langs.maths.generic.bool.ABoolExpr;
import visitors.SMTEncoder;

import static com.microsoft.z3.Status.SATISFIABLE;
import static com.microsoft.z3.Status.UNKNOWN;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:12
 */
public final class Z3 {

    private static final Context context = new Context();
    private static final Solver solver = context.mkSimpleSolver();

    private Z3() {
    }

    public static Z3Result checkSAT(ABoolExpr expr, DefsRegister defsRegister) {
        solver.reset();
        solver.add(expr.accept(new SMTEncoder(context, solver, defsRegister)));
        Status status = solver.check();
        if (status == UNKNOWN) {
            try {
                throw new Exception("Error: UNKNOWN RESULT WAS FOUND.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Z3Result(expr, status, status == SATISFIABLE ? solver.getModel() : null, context);
    }

}
