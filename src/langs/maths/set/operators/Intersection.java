package langs.maths.set.operators;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:22
 */
public final class Intersection extends ANarySetExpr {

    public Intersection(DefsRegister defsRegister, AFiniteSetExpr... operands) {
        super(defsRegister, operands);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(getOperands().stream().map(operand -> new InDomain(expr, operand)).toArray(ABoolExpr[]::new));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues() {
        return getOperands().get(0).getElementsValues().stream().filter(elementValue -> getOperands().subList(1, getOperands().size()).stream().allMatch(aFiniteSetExpr -> aFiniteSetExpr.getElementsValues().contains(elementValue))).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Intersection clone() {
        return new Intersection(new DefsRegister(getDefsRegister()), getOperands().stream().map(ASetExpr::clone).toArray(AFiniteSetExpr[]::new));
    }

}
