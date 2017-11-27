package langs.maths.generic.arith;

import langs.maths.generic.AGenericTypeExpr;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:58
 */
public abstract class AUnaryArithExpr<Operand extends AGenericTypeExpr> extends AArithExpr {

    private final Operand operand;

    public AUnaryArithExpr(Operand operand) {
        this.operand = operand;
    }

    public Operand getOperand() {
        return operand;
    }

}
