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
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:13
 */
public final class Difference extends ANarySetExpr {

    public Difference(AFiniteSetExpr... operands) {
        super(operands);
    }

    @Override
    public Difference accept(IPrimer primer) {
        return primer.visit(this);
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
    protected LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister) {
        return getOperands().get(0).getElementsValues(defsRegister).stream().filter(aValue -> !new Union(getOperands().subList(1, getOperands().size()).toArray(new AFiniteSetExpr[0])).getElementsValues(defsRegister).contains(aValue)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Difference cloned() {
        return new Difference(getOperands().stream().map(ASetExpr::cloned).toArray(AFiniteSetExpr[]::new));
    }

}
