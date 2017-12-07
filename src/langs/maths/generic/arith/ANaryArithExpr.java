package langs.maths.generic.arith;

import langs.maths.AExpr;
import langs.maths.generic.AGenericTypeExpr;
import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public abstract class ANaryArithExpr<Operand extends AGenericTypeExpr> extends AArithExpr {

    private final List<Operand> operands;

    protected ANaryArithExpr(Operand[] operands) {
        this.operands = Arrays.asList(operands);
    }

    @Override
    public abstract ANaryArithExpr<Operand> accept(IPrimer primer);

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return operands.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<Operand> getOperands() {
        return operands;
    }

    @Override
    public abstract ANaryArithExpr<Operand> clone();

}
