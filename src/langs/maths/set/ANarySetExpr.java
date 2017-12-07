package langs.maths.set;

import langs.maths.AExpr;
import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 00:52
 */
public abstract class ANarySetExpr extends AFiniteSetExpr {

    private final List<AFiniteSetExpr> operands;

    protected ANarySetExpr(AFiniteSetExpr[] operands) {
        this.operands = Arrays.asList(operands);
    }

    @Override
    public abstract ANarySetExpr accept(IPrimer primer);

    @Override
    public final LinkedHashSet<Fun> getFuns() {
        return operands.stream().map(AExpr::getFuns).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public final List<AFiniteSetExpr> getOperands() {
        return operands;
    }

    @Override
    public abstract ANarySetExpr clone();

}
