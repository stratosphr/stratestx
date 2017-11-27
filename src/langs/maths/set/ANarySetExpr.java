package langs.maths.set;

import langs.maths.AExpr;
import langs.maths.generic.arith.literals.Fun;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:52
 */
public abstract class ANarySetExpr extends ASetExpr {

    private final List<ASetExpr> operands;

    public ANarySetExpr(ASetExpr[] operands) {
        this.operands = Arrays.asList(operands);
    }

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return operands.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<ASetExpr> getOperands() {
        return operands;
    }

    @Override
    public abstract ANarySetExpr clone();

}
