package langs.maths.set.literals;

import langs.maths.generic.arith.AArithExpr;
import langs.maths.set.ASetExpr;
import visitors.interfaces.IObjectFormatter;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 23:19
 */
public final class Set extends ASetExpr {

    private final LinkedHashSet<AArithExpr> elements;

    public Set(AArithExpr... elements) {
        this.elements = new LinkedHashSet<>(Arrays.asList(elements));
    }

    @Override
    public String accept(IObjectFormatter visitor) {
        return visitor.visit(this);
    }

    public LinkedHashSet<AArithExpr> getElements() {
        return elements;
    }

}
