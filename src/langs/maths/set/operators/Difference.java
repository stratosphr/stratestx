package langs.maths.set.operators;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.And;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.generic.bool.operators.Not;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:13
 */
public final class Difference extends ANarySetExpr {

    public Difference(DefsRegister defsRegister, AFiniteSetExpr... operands) {
        super(defsRegister, operands);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new And(new InDomain(expr, getOperands().get(0)), new And(getOperands().subList(1, getOperands().size()).stream().map(operand -> new Not(new InDomain(expr, operand))).toArray(ABoolExpr[]::new)));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues() {
        return getOperands().get(0).getElementsValues().stream().filter(aValue -> !new Union(getDefsRegister(), getOperands().subList(1, getOperands().size()).toArray(new AFiniteSetExpr[0])).getElementsValues().contains(aValue)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Difference clone() {
        return new Difference(new DefsRegister(getDefsRegister()), getOperands().stream().map(ASetExpr::clone).toArray(AFiniteSetExpr[]::new));
    }

}
