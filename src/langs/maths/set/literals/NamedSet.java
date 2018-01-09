package langs.maths.set.literals;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.set.AFiniteSetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 03/12/17.
 * Time : 00:47
 */
public final class NamedSet extends AFiniteSetExpr {

    private final String name;
    private final AFiniteSetExpr set;

    public NamedSet(String name, AFiniteSetExpr set) {
        this.name = name;
        this.set = set;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public NamedSet accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return set.getFuns();
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister) {
        return set.getElementsValues(defsRegister);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return set.getConstraint(expr);
    }

    public String getName() {
        return name;
    }

    public AFiniteSetExpr getSet() {
        return set;
    }

    @Override
    public NamedSet cloned() {
        return new NamedSet(name, set.cloned());
    }

}
