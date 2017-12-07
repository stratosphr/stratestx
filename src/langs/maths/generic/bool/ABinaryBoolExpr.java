package langs.maths.generic.bool;

import langs.maths.AExpr;
import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:08
 */
public abstract class ABinaryBoolExpr<Left extends AExpr, Right extends AExpr> extends ABoolExpr {

    private final Left left;
    private final Right right;

    protected ABinaryBoolExpr(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public abstract ABinaryBoolExpr<Left, Right> accept(IPrimer primer);

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return Stream.of(left.getFuns(), right.getFuns()).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

    @Override
    public abstract ABinaryBoolExpr<Left, Right> clone();

}
