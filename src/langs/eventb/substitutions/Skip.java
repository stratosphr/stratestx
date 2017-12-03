package langs.eventb.substitutions;

import langs.maths.generic.arith.AAssignable;
import langs.maths.generic.bool.ABoolExpr;
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
        return null;
    }

    @Override
    public ASubstitution clone() {
        return new Skip();
    }

}
