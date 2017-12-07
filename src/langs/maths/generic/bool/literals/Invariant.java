package langs.maths.generic.bool.literals;

import com.microsoft.z3.BoolExpr;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;
import visitors.interfaces.ISMTEncoder;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 01:57
 */
public final class Invariant extends ABoolExpr {

    private final ABoolExpr expr;

    public Invariant(ABoolExpr expr) {
        this.expr = expr;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public BoolExpr accept(ISMTEncoder encoder) {
        return encoder.visit(this);
    }

    @Override
    public Invariant accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return expr.getFuns();
    }

    public ABoolExpr getExpr() {
        return expr;
    }

    @Override
    public Invariant clone() {
        return new Invariant(expr.clone());
    }

}
