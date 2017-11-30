package langs.maths.set;

import langs.maths.generic.arith.literals.Fun;
import visitors.interfaces.IPrimer;

import java.util.LinkedHashSet;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 15:26
 */
public abstract class AInfiniteSetExpr extends ASetExpr {

    @Override
    public abstract AInfiniteSetExpr accept(IPrimer primer);

    @Override
    public LinkedHashSet<Fun> getFuns() {
        return new LinkedHashSet<>();
    }

    @Override
    public abstract AInfiniteSetExpr clone();

}
