package langs.maths.generic.bool;

import langs.maths.AExpr;
import langs.maths.generic.AGenericTypeExpr;
import langs.maths.generic.arith.literals.Fun;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:55
 */
public abstract class ANaryBoolExpr<Operand extends AGenericTypeExpr> extends ABoolExpr {

    private final List<Operand> operands;

    public ANaryBoolExpr(Operand[] operands) {
        this.operands = Arrays.asList(operands);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return operands.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<Operand> getOperands() {
        return operands;
    }

    @Override
    public abstract ANaryBoolExpr<Operand> clone();

}
