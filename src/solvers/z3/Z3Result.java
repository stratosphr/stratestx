package solvers.z3;

import com.microsoft.z3.Context;
import com.microsoft.z3.Status;
import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;

import java.util.Set;

import static com.microsoft.z3.Status.*;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 22:14
 */
public final class Z3Result {

    private ABoolExpr expr;
    private final Status status;
    private final com.microsoft.z3.Model model;
    private final Context context;

    Z3Result(ABoolExpr expr, Status status, com.microsoft.z3.Model model, Context context) {
        this.expr = expr;
        this.status = status;
        this.model = model;
        this.context = context;
    }

    public boolean isUNSAT() {
        return status == UNSATISFIABLE;
    }

    public boolean isUNKNOWN() {
        return status == UNKNOWN;
    }

    public boolean isSAT() {
        return status == SATISFIABLE;
    }

    public Model getModel(Set<AAssignable> assignables) {
        if (!isSAT()) {
            throw new Error("Error: Unable to generate a model for non satisfiable expression \"" + expr + "\".");
        }
        return new Model(model, context, assignables);
    }

}
