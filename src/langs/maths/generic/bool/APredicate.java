package langs.maths.generic.bool;

import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 06/12/17.
 * Time : 14:20
 */
public abstract class APredicate extends ABoolExpr {

    private final String name;
    private final ABoolExpr expr;

    public APredicate(String name, ABoolExpr expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public abstract APredicate accept(IPrimer primer);

    @Override
    public final LinkedHashSet<Fun> getFuns() {
        return expr.getFuns();
    }

    public final String getName() {
        return name;
    }

    public final ABoolExpr getExpr() {
        return expr;
    }

}
