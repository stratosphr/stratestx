package langs.maths.generic.arith.literals;

import langs.maths.generic.arith.AArithExpr;

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
    public abstract AValue clone();

    public Integer getValue() {
        return value;
    }

}
