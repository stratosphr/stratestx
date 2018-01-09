package langs.maths.set.operators;

import langs.maths.def.DefsRegister;
import langs.maths.generic.arith.AArithExpr;
import langs.maths.generic.arith.literals.AValue;
import langs.maths.generic.bool.ABoolExpr;
import langs.maths.generic.bool.operators.InDomain;
import langs.maths.generic.bool.operators.Or;
import langs.maths.set.AFiniteSetExpr;
import langs.maths.set.ANarySetExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;
import visitors.interfaces.IPrimer;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:22
 */
public final class Union extends ANarySetExpr {

    public Union(AFiniteSetExpr... operands) {
        super(operands);
    }

    @Override
    public Union accept(IPrimer primer) {
        return primer.visit(this);
    }

    @Override
    public String accept(IObjectFormatter formatter) {
        return formatter.visit(this);
    }

    @Override
    public ABoolExpr getConstraint(AArithExpr expr) {
        return new Or(getOperands().stream().map(operand -> new InDomain(expr, operand)).toArray(ABoolExpr[]::new));
    }

    @Override
    protected LinkedHashSet<AValue> computeElementsValues(DefsRegister defsRegister) {
        return getOperands().stream().map(operand -> operand.getElementsValues(defsRegister)).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Union cloned() {
        return new Union(getOperands().stream().map(ASetExpr::cloned).toArray(AFiniteSetExpr[]::new));
    }

}
