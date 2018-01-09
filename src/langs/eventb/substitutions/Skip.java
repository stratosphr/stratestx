package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.Equals;
import visitors.Primer;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 02/12/17.
 * Time : 00:44
 */
public final class Skip extends ASubstitution {

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getPrd(LinkedHashSet<AAssignable> assignables) {
        return new And(assignables.stream().map(assignable -> new Equals(assignable.accept(new Primer(1)), assignable)).toArray(ABoolExpr[]::new));
    }

    @Override
    public Skip cloned() {
        return new Skip();
    }

}
