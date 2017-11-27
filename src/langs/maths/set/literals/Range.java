package langs.maths.set.literals;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.Fun;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.GEQ;
import langs.maths.generic.bool.operators.LEQ;
import langs.maths.set.AFiniteSetExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:18
 */
public final class Range extends AFiniteSetExpr {

    private final AArithExpr lowerBound;
    private final AArithExpr upperBound;

    public Range(AArithExpr lowerBound, AArithExpr upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return Stream.of(lowerBound.getFuns(), upperBound.getFuns()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public AArithExpr getLowerBound() {
        return lowerBound;
    }

    public AArithExpr getUpperBound() {
        return upperBound;
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(new GEQ(expr, lowerBound), new LEQ(expr, upperBound));
    }

    @Override
    public Range clone() {
        return new Range(lowerBound, upperBound);
    }

}
