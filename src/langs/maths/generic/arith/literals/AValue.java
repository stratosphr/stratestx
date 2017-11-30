package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;
import visitors.interfaces.IPrimer;

/**
 * Created by gvoiron on 27/11/17.
 * Time : 23:15
 */
public abstract class AValue extends AArithExpr {

    private final Integer value;

    AValue(int value) {
        this.value = value;
    }

    @Override
    public abstract AValue accept(IPrimer primer);

    public Integer getValue() {
        return value;
    }

    @Override
    public abstract AValue clone();

}
