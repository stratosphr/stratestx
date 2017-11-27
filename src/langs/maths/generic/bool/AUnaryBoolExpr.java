package langs.maths.generic.bool;

import langs.maths.generic.AGenericTypeExpr;

/**
 * Created by gvoiron on 26/11/17.
 * Time : 22:58
 */
public abstract class AUnaryBoolExpr<Operand extends AGenericTypeExpr> extends ABoolExpr {

    private Operand operand;

    public AUnaryBoolExpr(Operand operand) {
        this.operand = operand;
    }

    public Operand getOperand() {
        return operand;
    }

}
