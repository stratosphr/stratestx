package langs.maths.generic.arith;

import langs.maths.generic.AGenericTypeExpr;
import langs.maths.generic.arith.literals.Fun;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 03:09
 */
public abstract class ABinaryArithExpr<Left extends AGenericTypeExpr, Right extends AGenericTypeExpr> extends AArithExpr {

    private final Left left;
    private final Right right;

    public ABinaryArithExpr(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

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
    public abstract ABinaryArithExpr<Left, Right> clone();

}
