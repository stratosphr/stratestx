package langs.maths.set.literals;

import langs.maths.AExpr;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.Equals;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.AFiniteSetExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:19
 */
public final class Set extends AFiniteSetExpr {

    private final LinkedHashSet<AArithExpr> elements;

    public Set(AArithExpr... elements) {
        this.elements = new LinkedHashSet<>(Arrays.asList(elements));
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return elements.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public LinkedHashSet<AArithExpr> getElements() {
        return elements;
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new Or(elements.stream().map(element -> new Equals(expr, element)).toArray(ABoolExpr[]::new));
    }

    @Override
    public Set clone() {
        return new Set(elements.toArray(new AArithExpr[0]));
    }

}
